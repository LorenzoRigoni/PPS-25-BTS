package controllers

import models.rightDirections.RightDirectionsLogic
import models.{BrainAgeCalculator, CountWordsLogic, FastCalcLogic, MiniGameLogic}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import utils.MiniGames
import utils.MiniGames.CountWords
import utils.CountWordsConstants.{COUNT_WORDS_TURNS, COUNT_WORDS_DIFFICULTY_STEP}

class GameControllerTests extends AnyFunSuite with Matchers:

  test("Controller should initialize with all mini-games") {
    val controller = GameController()
    controller.remainingMiniGames.length shouldBe 4
    controller.currentGame shouldBe None
  }

  test("Controller should choose correct mini-game") {
    val controllerForFastCalc = GameController().chooseCurrentGame(MiniGames.FastCalc)
    controllerForFastCalc.currentGame.get shouldBe a [FastCalcLogic]

    val controllerForCountWords = GameController().chooseCurrentGame(MiniGames.CountWords)
    controllerForCountWords.currentGame.get shouldBe a [CountWordsLogic]

    val controllerForRightDirections = GameController().chooseCurrentGame(MiniGames.RightDirections)
    controllerForRightDirections.currentGame.get shouldBe a [RightDirectionsLogic]
  }

  test("Controller should generate question and record start time") {
    val controller            = GameController().chooseCurrentGame(CountWords)
    val (newController, question) = controller.getQuestion
    question should not be empty
  }

  test("Controller should check answer correctly") {
    val logic = CountWordsLogic(rounds = 1).generateQuestion._1.asInstanceOf[CountWordsLogic]
    val correctAnswer = logic.lastQuestion.get.split("\\s+").count(_.nonEmpty).toString
    
    val controller = GameController(
      currentGame = Some(logic),
      startTime = Some(System.currentTimeMillis())
    )
    
    controller.checkAnswer(correctAnswer)._2 shouldBe true
    controller.checkAnswer((correctAnswer.toInt + 1).toString)._2 shouldBe false
  }

  test("Controller should calculate brain age based on time and errors") {
    val questionResults = List(
      QuestionResult(1000, true),
      QuestionResult(2000, false),
      QuestionResult(800, true)
    )
    val gameStats       = GameStats(questionResults)

    val brainAge = BrainAgeCalculator.calcBrainAge(gameStats)
    brainAge shouldBe a [Int]
    assert(brainAge >= 20 && brainAge <= 100)
  }

  test("Controller should manage the end of the game") {
    var isFinished = false
    val callback   = new GameViewCallback:
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
