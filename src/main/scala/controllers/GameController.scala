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
import utils.QuestionResult

import scala.util.Random

extension (results: List[QuestionResult])
  def correctAnswers: Int     = results.count(_.isCorrect)
  def wrongAnswers: Int       = results.count(!_.isCorrect)
  def totalTimeInSeconds: Int = (results.map(_.responseTime).sum / 1000).toInt

/**
 * This case class represents the controller of the game. It manages the game loop and the
 * communications between logics and views.
 *
 * @param currentGame
 *   The mini-game playing at the moment
 * @param remainingMiniGames
 *   The mini-games yet to play
 * @param results
 *   The results of the test
 * @param numMiniGamesPlayed
 *   The number of mini-games played
 * @param startTime
 *   The initial time started when the question is generated
 * @param viewCallback
 *   The callback to the view to do when an event occurs
 */
case class GameController(
    currentGame: Option[MiniGameWrapper] = None,
    remainingMiniGames: List[MiniGames] = MiniGames.values.toList,
    results: List[QuestionResult] = List.empty,
    numMiniGamesPlayed: Int = 0,
    startTime: Option[Long] = None,
    viewCallback: Option[GameViewCallback] = None
):

  private val miniGamesFactory: Map[MiniGames, () => MiniGameWrapper] = Map(
    FastCalc        -> (() => MiniGameAdapter(FastCalcLogic(FAST_CALC_TURNS), FastCalc)),
    CountWords      -> (() => MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords)),
    RightDirections -> (() =>
      MiniGameAdapter(RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS), RightDirections)
    ),
    ColoredCount    -> (() => MiniGameAdapter(ColoredCountLogic(COLORED_COUNT_TURNS), ColoredCount)),
    WordMemory      -> (() => MiniGameAdapter(WordMemoryLogic(WORD_MEMORY_TURNS), WordMemory))
  )

  /**
   * Choose in a random way the next mini-game.
   * @return
   *   a copy of the controller with the mini-game to play
   */
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

  /**
   * Choose the mini-game to play.
   *
   * @param miniGame
   *   The mini-game to play
   * @return
   *   a copy of the controller with the mini-game to play
   */
  def chooseCurrentGame(miniGame: MiniGames): GameController =
    this.copy(currentGame = miniGamesFactory.get(miniGame).map(_.apply()))

  def getQuestion: (GameController, String) =
    val (updatedLogic, generatedQuestion) = currentGame.get.generateQuestion
    val updatedController                 = this.copy(
      currentGame = Some(updatedLogic),
      startTime = Some(System.currentTimeMillis())
    )
    (updatedController, generatedQuestion)

  /**
   * Check the answer of the user.
   *
   * @param answer
   *   The answer of the user
   * @return
   *   an Option with a copy of the controller and the correctness of the answer
   */
  def checkAnswer(answer: String): Option[(GameController, Boolean)] =
    for
      game      <- currentGame
      startTime <- this.startTime
    yield
      val parsedAnswer = game.getGameId match
        case FastCalc | CountWords | ColoredCount => answer.toInt
        case _                                    => answer

      val elapsedTime     = System.currentTimeMillis() - startTime
      val isAnswerCorrect = game.validateAnswer(parsedAnswer) match
        case b: Boolean            => b
        case d: Double if d >= 0.6 => true
        case _                     => false

      val updatedController = this.copy(
        currentGame = Some(game),
        results = QuestionResult(elapsedTime, isAnswerCorrect) :: results
      )
      (updatedController, isAnswerCorrect)

  /**
   * Check if the current mini-game is finished.
   *
   * @return
   *   true if it is finished, false otherwise
   */
  def isCurrentGameFinished: Boolean = currentGame.exists(_.isMiniGameFinished)

  /**
   * Calculate the brain age of the user.
   *
   * @return
   *   an Int that represents the brain age
   */
  def calculateBrainAge: Int = BrainAgeCalculator.calcBrainAge(results.reverse)

  /**
   * Get the number of the correct answers during the mini-game.
   *
   * @return
   *   the number of correct answers
   */
  def getNumberOfCorrectAnswers: Int = results.correctAnswers

  /**
   * Get the number of the wrong answers during the mini-game.
   *
   * @return
   *   the number of wrong answers
   */
  def getNumberOfWrongAnswers: Int = results.wrongAnswers

  /**
   * Get the total time used during the mini-game.
   *
   * @return
   *   the total time in seconds
   */
  def getTotalTime: Int = results.totalTimeInSeconds
