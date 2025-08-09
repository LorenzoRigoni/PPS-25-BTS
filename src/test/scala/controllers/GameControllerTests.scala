package controllers

import models.{BrainAgeCalculator, CountWordsLogic, MiniGameAdapter, MiniGameWrapper}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import utils.{MiniGames, QuestionResult}
import utils.MiniGames.CountWords
import utils.CountWordsConstants.COUNT_WORDS_TURNS
import utils.GameControllerConstants.{MAX_NUMBER_OF_MINIGAMES_AGE_TEST, NUM_OF_MINIGAMES_AVAILABLE}

/**
 * This class tests the game controller.
 */
class GameControllerTests extends AnyFunSuite with Matchers:

  test("Controller should initialize with all mini-games") {
    val controller = GameController()
    controller.remainingMiniGames.length shouldBe NUM_OF_MINIGAMES_AVAILABLE
    controller.currentGame shouldBe None
  }

  test("Controller should choose correct mini-game") {
    val controllerForFastCalc = GameController().chooseCurrentGame(MiniGames.FastCalc)
    controllerForFastCalc.currentGame.get shouldBe a[MiniGameWrapper]

    val controllerForCountWords = GameController().chooseCurrentGame(MiniGames.CountWords)
    controllerForCountWords.currentGame.get shouldBe a[MiniGameWrapper]

    val controllerForRightDirections = GameController().chooseCurrentGame(MiniGames.RightDirections)
    controllerForRightDirections.currentGame.get shouldBe a[MiniGameWrapper]

    val controllerForColoredCount = GameController().chooseCurrentGame(MiniGames.ColoredCount)
    controllerForColoredCount.currentGame.get shouldBe a[MiniGameWrapper]

    val controllerForWordMemory = GameController().chooseCurrentGame(MiniGames.WordMemory)
    controllerForWordMemory.currentGame.get shouldBe a[MiniGameWrapper]
  }

  test("Controller should generate question and record start time") {
    val controller                = GameController().chooseCurrentGame(CountWords)
    val (newController, question) = controller.getQuestion
    question should not be empty
  }

  test("Controller should check answer correctly") {
    val logic                         = MiniGameAdapter(CountWordsLogic(COUNT_WORDS_TURNS), CountWords)
    val controller                    = GameController(
      currentGame = Some(logic),
      startTime = Some(System.currentTimeMillis())
    )
    val (updatedController, question) = controller.getQuestion
    val correctAnswer                 = question.split("\\s+").count(_.nonEmpty).toString

    updatedController.checkAnswer(correctAnswer).get._2 shouldBe true
    updatedController.checkAnswer((correctAnswer.toInt + 1).toString).get._2 shouldBe false
  }

  test("Controller should calculate brain age based on time and errors") {
    val questionResults = List(
      QuestionResult(1000, true),
      QuestionResult(2000, false),
      QuestionResult(800, true)
    )

    val brainAge = BrainAgeCalculator.calcBrainAge(questionResults)
    brainAge shouldBe a[Int]
    assert(brainAge >= 20 && brainAge <= 100)
  }

  test("Controller should manage the end of the game") {
    var isFinished = false
    val callback   = new GameViewCallback:
      override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit = {}

      override def onGameFinished(controller: GameController): Unit = isFinished = true

    val controller = GameController(
      numMiniGamesPlayed = MAX_NUMBER_OF_MINIGAMES_AGE_TEST,
      viewCallback = Some(callback)
    )

    val next = controller.nextGame
    next.currentGame shouldBe Option.empty
    isFinished shouldBe true
  }
