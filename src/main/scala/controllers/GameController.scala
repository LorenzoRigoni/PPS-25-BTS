package controllers

import models.*
import models.rightDirections.RightDirectionsLogic
import utils.constants.CountWordsConstants.COUNT_WORDS_TURNS
import utils.constants.ColoredCountConstants.COLORED_COUNT_TURNS
import utils.constants.WordMemoryConstants.WORD_MEMORY_TURNS
import utils.constants.RightDirectionsConstants.*
import utils.constants.FastCalcConstants.*
import utils.enums.MiniGames.*
import utils.constants.GameControllerConstants.*
import utils.QuestionResult
import utils.Question
import utils.constants.FastCalcConstants
import utils.enums.MiniGames

import scala.util.Random

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
    currentGame: Option[(MiniGameLogic[_, _, _], MiniGames)] = None,
    remainingMiniGames: Seq[MiniGames] = MiniGames.values.toSeq,
    results: List[utils.QuestionResult] = List.empty,
    numMiniGamesPlayed: Int = 0,
    startTime: Option[Long] = None,
    viewCallback: Option[GameViewCallback] = None
):
  private val PERCENT_ACCETTABLE_ANSWER = 0.6
  private val SECONDS_UNITY             = 1000

  private val miniGamesFactory: Map[MiniGames, () => MiniGameLogic[_, _, _]] = Map(
    FastCalc        -> (() => FastCalcLogic(FAST_CALC_TURNS)),
    CountWords      -> (() => CountWordsLogic(COUNT_WORDS_TURNS)),
    RightDirections -> (() => RightDirectionsLogic(MAX_NUMBER_OF_ROUNDS)),
    ColoredCount    -> (() => ColoredCountLogic(COLORED_COUNT_TURNS)),
    WordMemory      -> (() => WordMemoryLogic(WORD_MEMORY_TURNS))
  )

  extension (results: List[utils.QuestionResult])
    private def correctAnswers: Int     = results.count(_.isCorrect)
    private def wrongAnswers: Int       = results.count(!_.isCorrect)
    private def totalTimeInSeconds: Int = (results.map(_.responseTime).sum / SECONDS_UNITY).toInt

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
      val nextMiniGame = remainingMiniGames(Random.nextInt(remainingMiniGames.size))
      this.copy(
        currentGame = miniGamesFactory.get(nextMiniGame).map(m => (m.apply(), nextMiniGame)),
        remainingMiniGames = remainingMiniGames.filterNot(_ == nextMiniGame),
        numMiniGamesPlayed = numMiniGamesPlayed + 1
      )

  /**
   * Choose the mini-game to play.
   *
   * @param miniGame
   *   The mini-game to play
   * @return
   *   a copy of the controller with the mini-game to play
   */
  def chooseCurrentGame(miniGame: MiniGames): GameController =
    this.copy(currentGame = miniGamesFactory.get(miniGame).map(m => (m.apply(), miniGame)))

  /**
   * Generate a new question of the mini-game.
   *
   * @return
   *   a copy of the controller and a new question
   */
  def getQuestion: (GameController, Question) =
    val (updatedLogic, generatedQuestion) = currentGame.get._1.generateQuestion
    val updatedController                 = this.copy(
      currentGame = Some((updatedLogic, currentGame.get._2)),
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
      startTime <- startTime
    yield
      val parsedAnswer      = game._1.parseAnswer(answer)
      val elapsedTime       = System.currentTimeMillis() - startTime
      val isAnswerCorrect   = game._1.validateAnswer(parsedAnswer) match
        case b: Boolean => b
        case d: Double  => d >= PERCENT_ACCETTABLE_ANSWER
        case _          => false
      val updatedController = this.copy(
        currentGame = Some(game),
        results = utils.QuestionResult(elapsedTime, isAnswerCorrect) :: results
      )
      (updatedController, isAnswerCorrect)

  /**
   * Check if the current mini-game is finished.
   *
   * @return
   *   true if it is finished, false otherwise
   */
  def isCurrentGameFinished: Boolean = currentGame.exists(_._1.isMiniGameFinished)

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
