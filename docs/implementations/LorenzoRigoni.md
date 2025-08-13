# Lorenzo Rigoni

Nel corso dello sviluppo mi sono occupato principalmente dell'implementazione delle seguenti *features*:

- controller del gioco
- test e logica del mini-gioco *Count Words*
- test e logica del mini-gioco *Colored Count*
- algoritmo per il calcolo dell'età mentale

Gli ultimi tre punti sono stati sviluppati prevalentemente in autonomia mentre il primo punto è stato fatto in
collaborazione con Alessandra Versari.

## Logica Count Words e Colored Count

Ogni mini-gioco implementato nel progetto deve aderire al contratto *MiniGameLogic* il quale prevede tre funzioni
principali da implementare:
- generare nuove domande
- fare il *parsing* delle risposte dell'utente
- validare le risposte dell'utente
- controllare se il mini-gioco è finito

Dato che i mini-giochi possono generare diversi tipi di domande, accettare diversi tipi di input e produrre diversi
tipi di risposta, il trait è stato implementato tramite l'uso dei *generics*. Inoltre, per garantire 
l'immutabilità, ogni volta che viene generata una nuova domanda deve essere restituita anche una copia dello stato
aggiornato.

```
trait MiniGameLogic[Q <: Question, A, B]:
  
  def generateQuestion: (MiniGameLogic[Q, A, B], Q)
  
  def parseAnswer(answer: String): A

  def validateAnswer(answer: A): B

  def isMiniGameFinished: Boolean
```

In questo caso, *A* e *B* sono due tipi generici che possono essere di qualsiasi tipo mentre *Q* (che rappresenta
il tipo di domanda) deve essere un sotto tipo del trait *Question*.

```
sealed trait Question

case class SimpleTextQuestion(text: String) extends Question

case class ColoredCountQuestion(
  numbersWithColor: Seq[(Int, ColoredCountColors)],
  colorRequired: ColoredCountColors
) extends Question
```

I due mini-giochi che ho sviluppato usano due tipi diversi di domanda generata. Infatti, *Count Words* genera domande
del tipo *SimpleTextQuestion* mentre *Colored Count* genera domande del tipo *ColoredCountQuestion*.
In entrambi i casi, i mini-giochi sono implementati come *case class* che hanno i seguenti campi:
- il numero di round totali
- il round attuale
- la difficoltà attuale
- l'ultima domanda generata

Nel caso di *Count Words*, le domande vengono generate aumentando il numero di parole da contare.

```
override def generateQuestion: (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
    val minRand        = math.max(1, difficulty - 1)
    val numOfWords     =
      if difficulty <= MIN_DIFFICULTY then MIN_NUMBER_WORDS + Random.between(0, difficulty + 1)
      else MIN_NUMBER_WORDS + Random.between(minRand, difficulty + 1)
    val wordsGenerated = Seq.fill(numOfWords)(WORDS(Random.nextInt(WORDS.size))).mkString(" ")
    val question       = SimpleTextQuestion(wordsGenerated)

    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + COUNT_WORDS_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

override def validateAnswer(answer: Int): Boolean =
    lastQuestion match
        case Some(q) => answer == q.text.split("\\s+").count(_.nonEmpty)
        case _       => false
```

Nel caso di *Colored Count*, invece, vengono aumentati i numeri mostrati.

```
override def generateQuestion: (MiniGameLogic[ColoredCountQuestion, Int, Boolean], ColoredCountQuestion) =
    val totalNumbers  = MIN_NUMBERS + difficulty * MULT_DIFFICULTY
    val numbers       = Seq.fill(totalNumbers)(Random.between(MIN_POSSIBLE_NUMBER, MAX_POSSIBLE_NUMBER))
    val colorList     = Seq.fill(totalNumbers)(
      ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    )
    val zipped        = numbers.zip(colorList)
    val questionColor = ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    val question      = ColoredCountQuestion(zipped, questionColor)

    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + COLORED_COUNT_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

override def validateAnswer(answer: Int): Boolean =
    lastQuestion match
        case Some(q) => answer == q.numbersWithColor.count((_, c) => c == q.colorRequired)
        case _       => false
```

## Algoritmo Brain Age
Per implementare l'algoritmo di calcolo dell'età è stato creato un oggetto *Singleton* che funzionasse come "helper".
Questo algoritmo prende in ingresso una lista (immutabile) di *QuestionResult* il quale contiene i tempi di risposta
e la correttezza delle risposte dell'utente. Il risultato viene calcolando partendo da un'età base (20 anni) a cui
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
    if results.isEmpty then return BASE_AGE
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
è stata creata una collezione di *MiniGames*, ovvero del *enum* che rappresenta i mini-giochi.

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
        currentGame = miniGamesFactory.get(nextMiniGame).map(_.apply()),
        remainingMiniGames = remainingMiniGames.filterNot(_ == nextMiniGame),
        numMiniGamesPlayed = numMiniGamesPlayed + 1
      )
      
def getQuestion: (GameController, Question) =
    val (updatedLogic, generatedQuestion) = currentGame.get.generateQuestion
    val updatedController                 = this.copy(
        currentGame = Some(updatedLogic),
        startTime = Some(System.currentTimeMillis())
    )
    (updatedController, generatedQuestion)

def checkAnswer(answer: String): Option[(GameController, Boolean)] =
    for
        game      <- currentGame
        startTime <- this.startTime
    yield
        val parsedAnswer = game.parseAnswer(answer)
        val elapsedTime     = System.currentTimeMillis() - startTime
        val isAnswerCorrect = game.validateAnswer(parsedAnswer) match
            case b: Boolean                                   => b
            case d: Double if d >= PERCENT_ACCETTABLE_ANSWER  => true
            case _                                            => false
        val updatedController = this.copy(
        currentGame = Some(game),
        results = utils.QuestionResult(elapsedTime, isAnswerCorrect) :: results)
        (updatedController, isAnswerCorrect)
```

[Torna indietro](../Implementazione.md)
