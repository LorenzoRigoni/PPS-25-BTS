package controllers

import models.rightDirections.RightDirectionsLogic
import models.{
  BrainAgeCalculator,
  ColoredCountLogic,
  CountWordsLogic,
  FastCalcLogic,
  QuestionResult,
  SimpleTextQuestion,
  WordMemoryLogic
}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import utils.enums.MiniGames.CountWords
import utils.constants.CountWordsConstants.COUNT_WORDS_TURNS
import utils.constants.GameControllerConstants.MAX_NUMBER_OF_MINIGAMES_AGE_TEST
import utils.constants.BrainAgeCalculatorConstants.{BASE_AGE, MAXIMUM_AGE}
import utils.enums.MiniGames

/**
 * This class tests the game controller.
 */
class GameControllerTests extends AnyFunSuite with Matchers:
  private val QUESTION_RESULTS = List(
    QuestionResult(1000, true),
    QuestionResult(2000, false),
    QuestionResult(800, true)
  )

  test("Controller should initialize with all mini-games") {
    val controller = GameController()
    controller.remainingMiniGames shouldBe MiniGames.values.toList
    controller.currentGame shouldBe None
  }

  test("Controller should choose correct mini-game") {
    val controllerForFastCalc        = GameController().chooseCurrentGame(MiniGames.FastCalc)
    val controllerForCountWords      = GameController().chooseCurrentGame(MiniGames.CountWords)
    val controllerForRightDirections = GameController().chooseCurrentGame(MiniGames.RightDirections)
    val controllerForColoredCount    = GameController().chooseCurrentGame(MiniGames.ColoredCount)
    val controllerForWordMemory      = GameController().chooseCurrentGame(MiniGames.WordMemory)
    controllerForFastCalc.currentGame.get._1 shouldBe a[FastCalcLogic]
    controllerForCountWords.currentGame.get._1 shouldBe a[CountWordsLogic]
    controllerForRightDirections.currentGame.get._1 shouldBe a[RightDirectionsLogic]
    controllerForColoredCount.currentGame.get._1 shouldBe a[ColoredCountLogic]
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
    val brainAge = BrainAgeCalculator.calcBrainAge(QUESTION_RESULTS)
    brainAge shouldBe a[Int]
    assert(brainAge >= BASE_AGE && brainAge <= MAXIMUM_AGE)
  }

  test("Controller should recognize the end of the game") {
    val controller = GameController(
      numMiniGamesPlayed = MAX_NUMBER_OF_MINIGAMES_AGE_TEST
    )
    val next       = controller.nextGame
    next.currentGame shouldBe Option.empty
  }
