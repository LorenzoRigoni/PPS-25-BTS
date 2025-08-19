# Lorenzo Rigoni

Nel corso dello sviluppo mi sono occupato principalmente dell'implementazione delle seguenti *features*:

- controller del gioco
- test e logica del mini-gioco *Count Words*
- test e logica del mini-gioco *Colored Count*
- algoritmo per il calcolo dell'età mentale

Gli ultimi tre punti sono stati sviluppati prevalentemente in autonomia mentre il primo punto è stato fatto in
collaborazione con Alessandra Versari.

## Logica Count Words e Colored Count

I due mini-giochi che ho sviluppato usano due tipi diversi di domanda generata. Infatti, *Count Words* genera domande
del tipo *SimpleTextQuestion* mentre *Colored Count* genera domande del tipo *ColoredCountQuestion*.

Nel caso di *Count Words*, le domande vengono generate aumentando il numero di parole da contare.

```
override protected def correctAnswer(question: SimpleTextQuestion): Int =
    question.text.split("\\s+").count(_.nonEmpty)

override def generateQuestion: (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
    val minRand        = math.max(1, difficulty - 1)
    val numOfWords     =
      if difficulty <= MIN_DIFFICULTY then MIN_NUMBER_WORDS + Random.between(0, difficulty + 1)
      else MIN_NUMBER_WORDS + Random.between(minRand, difficulty + 1)
    val wordsGenerated = Seq.fill(numOfWords)(WORDS(Random.nextInt(WORDS.size))).mkString(" ")
    val question       = SimpleTextQuestion(wordsGenerated)
    advance(question)
```

Nel caso di *Colored Count*, invece, vengono aumentati i numeri mostrati.

```
override protected def correctAnswer(question: ColoredCountQuestion): Int =
    question.numbersWithColor.count((_, c) => c == question.colorRequired)

override def generateQuestion: (MiniGameLogic[ColoredCountQuestion, Int, Boolean], ColoredCountQuestion) =
    val totalNumbers  = MIN_NUMBERS + difficulty * MULT_DIFFICULTY
    val numbers       = Seq.fill(totalNumbers)(Random.between(MIN_POSSIBLE_NUMBER, MAX_POSSIBLE_NUMBER))
    val colorList     = Seq.fill(totalNumbers)(
      ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    )
    val zipped        = numbers zip colorList
    val questionColor = ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    val question      = ColoredCountQuestion(zipped, questionColor)
    advance(question)
```

## Algoritmo Brain Age
Per implementare l'algoritmo di calcolo dell'età è stato creato un oggetto *Singleton* che funzionasse come "helper".
Questo algoritmo prende in ingresso una lista (immutabile) di *QuestionResult* il quale contiene i tempi di risposta
e la correttezza delle risposte dell'utente. Il risultato viene calcolato partendo da un'età base (20 anni) a cui
vengono sommate delle penalità per tempo ed errori.

```
case class QuestionResult (responseTime: Long, isCorrect: Boolean)
```

```
object BrainAgeCalculator:
  private val BASE_AGE = 20
  private val MAXIMUM_AGE = 100
  private val SECONDS_UNITY = 1000
  private val ERROR_PERCENT = 50

  def calcBrainAge(results: List[QuestionResult]): Int =
    if results.isEmpty then return MAXIMUM_AGE
    val avgTime      = results.map(_.responseTime).sum.toDouble / results.length
    val errorRate    = results.count(!_.isCorrect).toDouble / results.length
    val timePenalty  = (avgTime / SECONDS_UNITY).toInt
    val errorPenalty = (errorRate * ERROR_PERCENT).toInt
    val result       = baseAge + timePenalty + errorPenalty
    if result <= MAXIMUM_AGE then result else MAXIMUM_AGE
```

## Controller

Il controller rappresenta il punto centrale del progetto. Infatti, si tratta dell'entità che coordina la grafica
e la logica nel pieno spirito *MVC*. Per fare ciò, è stato implementata una *case class GameController* la quale
ha i seguenti campi:
- il mini-gioco attualmente in gioco
- i rimanenti mini-giochi non ancora giocati
- i risultati dell'utente
- il numero di mini-game giocati nell'Age Test fino a quel momento
- il tempo iniziale
- la callback per richiamare la view all'accadere di un evento

Una prima difficoltà incontrata è stata riguardo alla collezione delle logiche dei mini-giochi. Infatti, in
Scala, non è possibile riempire una collezione con trait che usano i *generics* i quali vengono implementati
in maniere differenti. Per ovviare a questo problema, non è stata creata una collezione di *MiniGameLogic* bensì
è stata creata una collezione di *MiniGames*, ovvero di tipi definiti nell'*enum* che rappresenta i mini-giochi.

```
enum MiniGames(val displayName: String):
  case FastCalc        extends MiniGames("Fast Calc")
  case CountWords      extends MiniGames("Count Words")
  case RightDirections extends MiniGames("Right Directions")
  case ColoredCount    extends MiniGames("Colored Count")
  case WordMemory      extends MiniGames("Word Memory")
```

In questo modo, i mini-giochi vengono poi creati tramite una *factory*.

```
private val miniGamesFactory: Map[MiniGames, () => MiniGameLogic[_, _, _]] = Map(
    FastCalc        -> (() => FastCalcLogic(FAST_CALC_TURNS)),
    CountWords      -> (() => CountWordsLogic(COUNT_WORDS_TURNS)),
    RightDirections -> (() => RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS)),
    ColoredCount    -> (() => ColoredCountLogic(COLORED_COUNT_TURNS)),
    WordMemory      -> (() => WordMemoryLogic(WORD_MEMORY_TURNS))
  )
  
def chooseCurrentGame(miniGame: MiniGames): GameController =
    this.copy(currentGame = miniGamesFactory.get(miniGame).map(m => (m.apply(), miniGame)))
```

Inoltre, dato che per il training vengono chiesti dei risultati riguardo alle risposte dell'utente, è stato implementato
una *extension* con i metodi utili.

```
extension (results: List[utils.QuestionResult])
  def correctAnswers: Int             = results.count(_.isCorrect)
  def wrongAnswers: Int               = results.count(!_.isCorrect)
  def totalTimeInSeconds: Int         = (results.map(_.responseTime).sum / SECONDS_UNITY).toInt
```

Infine, sono stati implementati i metodi per la scelta del nuovo mini-gioco, la generazione della domanda e il controllo
della risposta.

```
def nextGame: GameController =
    if numMiniGamesPlayed == MAX_NUMBER_OF_MINIGAMES_AGE_TEST then
      val finalController = this.copy(currentGame = None)
      viewCallback.foreach(_.onGameFinished(finalController))
      finalController
    else
      val nextMiniGame = remainingMiniGames(Random.nextInt(remainingMiniGames.size))
      this.copy(
        currentGame = miniGamesFactory.get(nextMiniGame).map(m => (m.apply(), nextMiniGame)),
        remainingMiniGames = remainingMiniGames.filterNot(_ == nextMiniGame),
        numMiniGamesPlayed = numMiniGamesPlayed + 1
      )
      
def getQuestion: (GameController, Question) =
    val (updatedLogic, generatedQuestion) = currentGame.get._1.generateQuestion
    val updatedController                 = this.copy(
      currentGame = Some((updatedLogic, currentGame.get._2)),
      startTime = Some(System.currentTimeMillis())
    )
    (updatedController, generatedQuestion)

def checkAnswer(answer: String): Option[(GameController, Boolean)] =
    for
      game  <- currentGame
      start <- startTime
    yield
      val elapsedTime       = System.currentTimeMillis() - start
      val isAnswerCorrect   = game._1.parseAnswer(answer) match
        case Some(parsedAnswer) =>
          game._1.validateAnswer(parsedAnswer) match
            case b: Boolean => b
            case d: Double  => d >= PERCENT_ACCETTABLE_ANSWER
            case _          => false
        case _                  => false
      val updatedController = this.copy(
        currentGame = Some(game),
        results = utils.QuestionResult(elapsedTime, isAnswerCorrect) :: results
      )
      (updatedController, isAnswerCorrect)
```

In questo ultimo metodo, *checkAnswer*, le risposte non previste (stringhe vuote, input non numerici...) vengono
trattate direttamente come risposte errate, andando così a gestire eventuali eccezioni.

[Torna indietro](../Implementazione.md)
