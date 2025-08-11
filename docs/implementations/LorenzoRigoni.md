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
- validare le risposte dell'utente
- controllare se il mini-gioco è finito

Dato che i mini-giochi possono generare diversi tipi di domande, accettare diversi tipi di input e produrre diversi
tipi di risposta, il trait è stato implementato tramite l'uso dei *generics*. Inoltre, per garantire 
l'immutabilità, ogni volta che viene generata una nuova domanda deve essere restituita anche una copia dello stato
aggiornato.

```scala 3
trait MiniGameLogic[Q <: Question, A, B]:
  
  def generateQuestion: (MiniGameLogic[Q, A, B], Q)

  def validateAnswer(answer: A): B

  def isMiniGameFinished: Boolean
```
In questo caso, *A* e *B* sono due tipi generici che possono essere di qualsiasi tipo mentre *Q* (che rappresenta
il tipo di domanda) deve essere un sotto tipo del trait *Question*.

```scala 3
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

```scala 3
override def generateQuestion
  : (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
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
    case Some(q) => answer == q.get.text.split("\\s+").count(_.nonEmpty)
    case _       => false
```

Nel caso di *Colored Count*, invece, vengono aumentati i numeri mostrati.

```scala 3
override def generateQuestion
      : (MiniGameLogic[ColoredCountQuestion, Int, Boolean], ColoredCountQuestion) =
    val totalNumbers  = MIN_NUMBERS + difficulty * 2
    val numbers       = List.fill(totalNumbers)(Random.between(1, 10))
    val colorList     = List.fill(totalNumbers)(
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

```scala 3
case class QuestionResult (responseTime: Long, isCorrect: Boolean)
```

```scala 3
object BrainAgeCalculator:

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
in maniere differenti. Per ovviare a questo problema, sono stati implementate due entità: *MiniGameWrapper* e
*MiniGameAdapter*.

*MiniGameWrapper* è un *trait* il quale racchiude le funzioni principali dei mini-giochi più due funzioni utili.

```scala 3
trait MiniGameWrapper[Q <: Question, A, B]:
    def generateQuestion: (MiniGameWrapper[Q, A, B], Q)
    
    def validateAnswer(answer: A): B
    
    def isMiniGameFinished: Boolean
    
    def getGameId: MiniGames
    
    def parseAnswer(input: String): A
```

*MiniGameAdapter* è una classe che implementa *MiniGameWrapper* e ha come campi la logica del mini-gioco, il suo tipo
(rappresentato da un *enum*) e il parser per la risposta dell'utente.

```scala 3
class MiniGameAdapter[Q <: Question, A, B](
  val logic: MiniGameLogic[Q, A, B],
  val gameId: MiniGames,
  val parser: String => A
) extends MiniGameWrapper[Q, A, B]:

override def generateQuestion: (MiniGameWrapper[Q, A, B], Q) =
  val (newLogic, question) = logic.generateQuestion
  (MiniGameAdapter(newLogic, getGameId, parser), question)

override def validateAnswer(answer: A): B = logic.validateAnswer(answer)

override def isMiniGameFinished: Boolean = logic.isMiniGameFinished

override def parseAnswer(input: String): A = parser(input)
```

In questo modo, è stato possibile creare una *List* dei mini-giochi, la quale viene creata grazie ad una *factory*.

```scala 3
private val miniGamesFactory: Map[MiniGames, () => MiniGameWrapper[_, _, _]] = Map(
  FastCalc        -> (() => MiniGameAdapter(FastCalcLogic(FAST_CALC_TURNS), FastCalc, _.toInt)),
  CountWords      -> (() => MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords, _.toInt)),
  RightDirections -> (() => MiniGameAdapter(RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS), RightDirections, identity)),
  ColoredCount    -> (() => MiniGameAdapter(ColoredCountLogic(COLORED_COUNT_TURNS), ColoredCount, _.toInt)),
  WordMemory      -> (() => MiniGameAdapter(WordMemoryLogic(WORD_MEMORY_TURNS), WordMemory, identity))
)

def chooseCurrentGame(miniGame: MiniGames): GameController =
  this.copy(currentGame = miniGamesFactory.get(miniGame).map(_.apply()))
```

Inoltre, dato che per il training vengono chiesti dei risultati riguardo alle risposte dell'utente, è stato implementato
una *extension* con i metodi utili.

```scala 3
extension (results: List[utils.QuestionResult])
  def correctAnswers: Int             = results.count(_.isCorrect)
  def wrongAnswers: Int               = results.count(!_.isCorrect)
  def totalTimeInSeconds: Int         = (results.map(_.responseTime).sum / SECONDS_UNITY).toInt
```

Infine, sono stati implementati i metodi per la scelta del nuovo mini-gioco, la generazione della domanda e il controllo
della risposta.

```scala 3
def nextGame: GameController =
    Option
      .when(numMiniGamesPlayed < MAX_NUMBER_OF_MINIGAMES_AGE_TEST) {
        val nextMiniGame = remainingMiniGames(Random.nextInt(remainingMiniGames.size))
        this.copy(
          currentGame = miniGamesFactory.get(nextMiniGame).map(_.apply()),
          remainingMiniGames = remainingMiniGames.filterNot(_ == nextMiniGame),
          numMiniGamesPlayed = numMiniGamesPlayed + 1
        )
      }
      .getOrElse {
        val finalController = this.copy(currentGame = None)
        viewCallback.foreach(_.onGameFinished(finalController))
        finalController
      }

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
        results = utils.QuestionResult(elapsedTime, isAnswerCorrect) :: results
      )
      (updatedController, isAnswerCorrect)
```
