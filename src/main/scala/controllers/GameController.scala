package controllers

import models.*
import models.rightDirections.RightDirectionsLogic
import utils.CountWordsConstants.{COUNT_WORDS_TURNS, COUNT_WORDS_DIFFICULTY_STEP}
import utils.ColoredCountConstants.{COLORED_COUNT_TURNS, COLORED_COUNT_DIFFICULTY_STEP}
import utils.RightDirectionsConstants.*
import utils.FastCalcConstants.*
import utils.{FastCalcConstants, MiniGames}
import utils.MiniGames.*

import java.util.concurrent.atomic.AtomicInteger
import java.util.{Timer, TimerTask}
import javax.swing.SwingUtilities
import scala.util.Random

case class QuestionResult(responseTime: Long, isCorrect: Boolean)

case class GameStats(results: List[QuestionResult])

/**
 * This case class represents the controller of the game. It manages the game loop and the
 * communications between logics and views.
 * @param remainingMiniGames
 *   The mini-games not played yet
 * @param currentGame
 *   The mini-game playing
 * @param lastQuestion
 *   The last question of the mini-game
 * @param difficulty
 *   The difficulty of the level
 * @param rand
 *   The random variable for choosing the next mini-game
 * @param timer
 *   The timer of the mini-games
 * @param timeLeft
 *   The time passed
 * @param viewCallback
 *   The methods to call when an event occurs
 */
case class GameController(
    remainingMiniGames: List[MiniGameWrapper] = List(
      MiniGameAdapter(FastCalcLogic(FAST_CALC_TURNS, 0, FAST_CALC_DIFFICULTY_STEP), FastCalc),
      MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords),
      MiniGameAdapter(RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS), RightDirections),
      MiniGameAdapter(ColoredCountLogic(COLORED_COUNT_TURNS), ColoredCount),
      MiniGameAdapter(WordMemoryLogic(10), WordMemory)
    ),
    numMiniGamesPlayed: Int = 0,
    currentGame: Option[MiniGameWrapper] = None,
    results: List[QuestionResult] = List(),
    timer: Option[Timer] = None,
    timeLeft: Int = 10, // TODO: 120
    viewCallback: Option[GameViewCallback] = None,
    startTime: Option[Long] = None
):

  private def startTimer(): GameController =
    timer.foreach(_.cancel())
    val t       = new Timer()
    val seconds = new AtomicInteger(timeLeft) // TODO: 120
    val task    = new TimerTask {
      override def run(): Unit =
        val remaining = seconds.decrementAndGet()
        viewCallback.foreach(_.onTimerUpdate(remaining))
        if remaining <= 0 then
          t.cancel()
          SwingUtilities.invokeLater(() => {
            val next = nextGame
            println(
              s"[nextGame] Gontroller restituito da nextGame (thread a parte) : ${next.results}"
            )
            next.chooseNextGame()
          })
    }
    t.scheduleAtFixedRate(task, 1000, 1000)
    this.copy(timer = Some(t), timeLeft = 10, results = this.results) // TODO: 120

  /**
   * Choose in a random way the next mini-game.
   * @return
   *   a copy of the controller with the mini-game to play
   */
  def nextGame: GameController =
    println(s"[nextGame] Chiamato con results: $results")
    if numMiniGamesPlayed == 3 then
      timer.foreach(_.cancel())
      val finalController = this.copy(currentGame = None)
      println(s"[nextGame] Gioco finito. Risultati totali: ${finalController.results}")
      viewCallback.foreach(_.onGameFinished(finalController))
      finalController
    else
      val nextMiniGame = remainingMiniGames(Random.nextInt(remainingMiniGames.size))
      val updatedList  = remainingMiniGames.filterNot(_ == nextMiniGame)

      println(s"[nextGame] Nuovo gioco scelto: ${nextMiniGame.gameId}")
      println(s"[nextGame] Risultati finora: $results")

      val controllerCopy =
        this.copy(
          currentGame = Some(nextMiniGame),
          remainingMiniGames = updatedList,
          numMiniGamesPlayed = numMiniGamesPlayed + 1,
          results = this.results
        )
      controllerCopy.startTimer()

  def chooseNextGame(): Unit =
    currentGame match
      case Some(game) =>
        viewCallback.foreach(_.onGameChanged(game.gameId, this))
      case None       => ()
    println(s"[chooseNextGame] Risultati dopo chooseNextGame: $results")

  def chooseCurrentGame(gameMode: MiniGames): GameController =
    val gameWrapper = gameMode match
      case FastCalc =>
        Some(
          MiniGameAdapter(FastCalcLogic(FAST_CALC_TURNS, 0, FAST_CALC_DIFFICULTY_STEP), FastCalc)
        )

      case CountWords =>
        Some(MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords))

      case RightDirections =>
        Some(MiniGameAdapter(RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS), RightDirections))

      case ColoredCount =>
        Some(MiniGameAdapter(ColoredCountLogic(COLORED_COUNT_TURNS), ColoredCount))

      case WordMemory =>
        Some(MiniGameAdapter(WordMemoryLogic(10), WordMemory))

    val ctrl = this.copy(currentGame = gameWrapper, timeLeft = 10) // TODO: 120
    println(s"[chooseCurrentGame] Risultati dopo chooseCurrentGame: $ctrl.results")
    ctrl

  def getQuestion: (GameController, String) =
    println(s"[getQuestion] Generazione nuova domanda")
    val (updatedLogic, generatedQuestion) = currentGame.get.generateQuestion
    val updatedController                 = this.copy(
      currentGame = Some(updatedLogic),
      startTime = Some(System.currentTimeMillis())
    )
    println(s"[getQuestion] Domanda generata. Results: ${updatedController.results}")
    (updatedController, generatedQuestion)

  def checkAnswer(answer: String): (GameController, Boolean) =
    println(s"[checkAnswer] Risposta ricevuta: $answer")

    val parsedAnswer = currentGame match
      case Some(wrapper) =>
        wrapper.gameId match
          case FastCalc | CountWords | ColoredCount => answer.toInt
          case _                                    => answer
      case None          => answer

    val logic           = currentGame.get
    val result          = logic.validateAnswer(parsedAnswer)
    val isAnswerCorrect = result match
      case b: Boolean            => b
      case v: Double if v >= 0.6 => true
      case _                     => false

    val elapsedTime = System.currentTimeMillis() - startTime.getOrElse(System.currentTimeMillis())
    val newResult   = QuestionResult(elapsedTime, isAnswerCorrect)

    println(s"[checkAnswer] Tempo risposta: $elapsedTime ms, Corretto: $isAnswerCorrect")
    println(s"[checkAnswer] Results prima update: $results")

    val updatedController = this.copy(
      currentGame = Some(logic),
      results = newResult :: results
    )

    println(s"[checkAnswer] Results dopo update: ${updatedController.results}")

    (updatedController, isAnswerCorrect)

  def isCurrentGameFinished: Boolean = currentGame.exists(_.isMiniGameFinished)

  def calculateBrainAge: Int = BrainAgeCalculator.calcBrainAge(GameStats(results.reverse))
