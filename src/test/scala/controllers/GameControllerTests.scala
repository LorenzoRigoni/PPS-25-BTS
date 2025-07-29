package controllers

import models.rightDirections.RightDirectionsLogic
import models.{BrainAgeCalculator, CountWordsLogic, FastCalcLogic, MiniGameLogic}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import utils.MiniGames
import utils.MiniGames.CountWords

class GameControllerTests extends AnyFunSuite with Matchers:
  private val TEST_QUESTION = "This is a test sentence"
  private val RIGHT_ANSWER = "5"
  private val WRONG_ANSWER = "6"

  test("Controller should initialize with all mini-games") {
    val controller = GameController()
    controller.remainingMiniGames.length shouldBe 3
    controller.currentGame shouldBe None
  }

  test("Controller should choose correct mini-game") {
    val controllerForFastCalc = GameController().chooseCurrentGame(MiniGames.FastCalc)
    controllerForFastCalc.currentGame shouldBe Some(FastCalcLogic)

    val controllerForCountWords = GameController().chooseCurrentGame(MiniGames.CountWords)
    controllerForCountWords.currentGame shouldBe Some(CountWordsLogic)

    val controllerForRightDirections = GameController().chooseCurrentGame(MiniGames.RightDirections)
    controllerForRightDirections.currentGame shouldBe Some(RightDirectionsLogic)
  }

  test("Controller should generate question and record start time") {
    val controller = GameController().chooseCurrentGame(CountWords)
    val (question, startTime) = controller.getQuestion
    question should not be empty
    startTime should be <= System.currentTimeMillis()
  }

  test("Controller should check answer correctly") {
    val controller = GameController(
      currentGame = Some(CountWordsLogic),
      lastQuestion = Some(TEST_QUESTION),
      startTime = System.currentTimeMillis()
    )

    controller.checkAnswer(RIGHT_ANSWER) shouldBe true
    controller.checkAnswer(WRONG_ANSWER) shouldBe false
  }

  test("Controller should calculate brain age based on time and errors") {
    val questionResults = List(
      QuestionResult(1000, true),
      QuestionResult(2000, false),
      QuestionResult(800, true)
    )
    val gameStats = GameStats(questionResults)

    val brainAge = BrainAgeCalculator.calcBrainAge(gameStats)
    brainAge shouldBe a[Int]
    assert(brainAge >= 20 && brainAge <= 100)
  }

  test("Controller should manage the end of the game") {
    var isFinished = false
    val callback = new GameViewCallback:
      override def onTimerUpdate(secondsLeft: Int): Unit = {}

      override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit = {}

      override def onGameFinished(controller: GameController): Unit = isFinished = true

    val controller = GameController(
      remainingMiniGames = List.empty,
      viewCallback = Some(callback)
    )

    val next = controller.nextGame
    next.currentGame shouldBe Option.empty
    isFinished shouldBe true
  }