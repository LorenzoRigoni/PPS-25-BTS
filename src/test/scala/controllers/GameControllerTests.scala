package controllers

import models.rightDirections.RightDirectionsLogic
import models.{
  BrainAgeCalculator,
  ColoredCountLogic,
  CountWordsLogic,
  FastCalcLogic,
  WordMemoryLogic
}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import utils.{QuestionResult, SimpleTextQuestion}
import utils.enums.MiniGames.CountWords
import utils.constants.CountWordsConstants.COUNT_WORDS_TURNS
import utils.constants.GameControllerConstants.MAX_NUMBER_OF_MINIGAMES_AGE_TEST
import utils.enums.MiniGames

/**
 * This class tests the game controller.
 */
class GameControllerTests extends AnyFunSuite with Matchers:

  test("Controller should initialize with all mini-games") {
    val controller = GameController()
    controller.remainingMiniGames shouldBe MiniGames.values.toList
    controller.currentGame shouldBe None
  }

  test("Controller should choose correct mini-game") {
    val controllerForFastCalc        = GameController().chooseCurrentGame(MiniGames.FastCalc)
    controllerForFastCalc.currentGame.get._1 shouldBe a[FastCalcLogic]
    val controllerForCountWords      = GameController().chooseCurrentGame(MiniGames.CountWords)
    controllerForCountWords.currentGame.get._1 shouldBe a[CountWordsLogic]
    val controllerForRightDirections = GameController().chooseCurrentGame(MiniGames.RightDirections)
    controllerForRightDirections.currentGame.get._1 shouldBe a[RightDirectionsLogic]
    val controllerForColoredCount    = GameController().chooseCurrentGame(MiniGames.ColoredCount)
    controllerForColoredCount.currentGame.get._1 shouldBe a[ColoredCountLogic]
    val controllerForWordMemory      = GameController().chooseCurrentGame(MiniGames.WordMemory)
    controllerForWordMemory.currentGame.get._1 shouldBe a[WordMemoryLogic]
  }

  test("Controller should generate question and record start time") {
    val controller                = GameController().chooseCurrentGame(CountWords)
    val (newController, question) = controller.getQuestion
    question.asInstanceOf[SimpleTextQuestion].text should not be empty
  }

  test("Controller should check answer correctly") {
    val logic                         = CountWordsLogic(COUNT_WORDS_TURNS)
    val controller                    = GameController(
      currentGame = Some((logic, CountWords)),
      startTime = Some(System.currentTimeMillis())
    )
    val (updatedController, question) = controller.getQuestion
    val correctAnswer                 =
      question.asInstanceOf[SimpleTextQuestion].text.split("\\s+").count(_.nonEmpty).toString
    updatedController.checkAnswer(correctAnswer).get._2 shouldBe true
    updatedController.checkAnswer((correctAnswer.toInt + 1).toString).get._2 shouldBe false
  }

  test("Controller should calculate brain age based on time and errors") {
    val questionResults = List(
      QuestionResult(1000, true),
      QuestionResult(2000, false),
      QuestionResult(800, true)
    )
    val brainAge        = BrainAgeCalculator.calcBrainAge(questionResults)
    brainAge shouldBe a[Int]
    assert(brainAge >= 20 && brainAge <= 100)
  }

  test("Controller should manage the end of the game") {
    var isFinished = false
    val callback   = new GameViewCallback:
      override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit = {}
      override def onGameFinished(controller: GameController): Unit                     = isFinished = true
    val controller = GameController(
      numMiniGamesPlayed = MAX_NUMBER_OF_MINIGAMES_AGE_TEST,
      viewCallback = Some(callback)
    )
    val next       = controller.nextGame
    next.currentGame shouldBe Option.empty
    isFinished shouldBe true
  }
