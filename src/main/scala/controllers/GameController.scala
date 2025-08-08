package controllers

import models.*
import models.rightDirections.RightDirectionsLogic
import utils.CountWordsConstants.COUNT_WORDS_TURNS
import utils.ColoredCountConstants.COLORED_COUNT_TURNS
import utils.WordMemoryConstants.WORD_MEMORY_TURNS
import utils.RightDirectionsConstants.*
import utils.FastCalcConstants.*
import utils.{FastCalcConstants, MiniGames}
import utils.MiniGames.*
import utils.GameControllerConstants.*

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
      MiniGameAdapter(FastCalcLogic(FAST_CALC_TURNS), FastCalc),
      MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords),
      MiniGameAdapter(RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS), RightDirections),
      MiniGameAdapter(ColoredCountLogic(COLORED_COUNT_TURNS), ColoredCount),
      MiniGameAdapter(WordMemoryLogic(WORD_MEMORY_TURNS), WordMemory)
    ),
    numMiniGamesPlayed: Int = 0,
    currentGame: Option[MiniGameWrapper] = None,
    results: List[QuestionResult] = List.empty,
    viewCallback: Option[GameViewCallback] = None,
    startTime: Option[Long] = None
):

  /**
   * Choose in a random way the next mini-game.
   * @return
   *   a copy of the controller with the mini-game to play
   */
  def nextGame: GameController =
    if numMiniGamesPlayed == MAX_NUMBER_OF_MINIGAMES_AGE_TEST then
      val finalController = this.copy(currentGame = None)
      viewCallback.foreach(_.onGameFinished(finalController))
      finalController
    else
      val nextMiniGame   = remainingMiniGames(Random.nextInt(remainingMiniGames.size))
      val updatedList    = remainingMiniGames.filterNot(_ == nextMiniGame)
      val controllerCopy =
        this.copy(
          currentGame = Some(nextMiniGame),
          remainingMiniGames = updatedList,
          numMiniGamesPlayed = numMiniGamesPlayed + 1
        )
      controllerCopy

  def chooseNextGame(): Unit =
    currentGame match
      case Some(game) =>
        viewCallback.foreach(_.onGameChanged(game.getGameId, this))
      case None       => ()

  def chooseCurrentGame(gameMode: MiniGames): GameController =
    val gameWrapper = gameMode match
      case FastCalc =>
        Some(MiniGameAdapter(FastCalcLogic(FAST_CALC_TURNS), FastCalc))

      case CountWords =>
        Some(MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords))

      case RightDirections =>
        Some(MiniGameAdapter(RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS), RightDirections))

      case ColoredCount =>
        Some(MiniGameAdapter(ColoredCountLogic(COLORED_COUNT_TURNS), ColoredCount))

      case WordMemory =>
        Some(MiniGameAdapter(WordMemoryLogic(WORD_MEMORY_TURNS), WordMemory))

    val ctrl = this.copy(currentGame = gameWrapper)
    ctrl

  def getQuestion: (GameController, String) =
    val (updatedLogic, generatedQuestion) = currentGame.get.generateQuestion
    val updatedController                 = this.copy(
      currentGame = Some(updatedLogic),
      startTime = Some(System.currentTimeMillis())
    )
    (updatedController, generatedQuestion)

  def checkAnswer(answer: String): (GameController, Boolean) =
    val parsedAnswer = currentGame match
      case Some(wrapper) =>
        wrapper.getGameId match
          case FastCalc | CountWords | ColoredCount => answer.toInt
          case _                                    => answer
      case None          => answer

    val logic           = currentGame.get
    val result          = logic.validateAnswer(parsedAnswer)
    val isAnswerCorrect = result match
      case b: Boolean            => b
      case v: Double if v >= 0.6 => true
      case _                     => false

    val elapsedTime       = System.currentTimeMillis() - startTime.getOrElse(System.currentTimeMillis())
    val newResult         = QuestionResult(elapsedTime, isAnswerCorrect)
    val updatedController = this.copy(
      currentGame = Some(logic),
      results = newResult :: results
    )
    (updatedController, isAnswerCorrect)

  def isCurrentGameFinished: Boolean = currentGame.exists(_.isMiniGameFinished)

  def calculateBrainAge: Int = BrainAgeCalculator.calcBrainAge(GameStats(results.reverse))
