package models

import org.scalatest.funsuite.AnyFunSuite
import utils.constants.FastCalcConstants.{FAST_CALC_DIFFICULTY_STEP, FAST_CALC_TURNS}
import utils.SimpleTextQuestion
import org.scalatest.matchers.should.Matchers

/**
 * This class tests the logic of the mini-game Fast Calc.
 */
class FastCalcTests extends AnyFunSuite with Matchers:
  private val TEST_DIFFICULTY_INDEX           = 3
  private val TEST_EXPRESSION_POSITIVE_RESULT = SimpleTextQuestion("8 + 4 * 2")
  private val logic                           = FastCalcLogic(
    rounds = FAST_CALC_TURNS,
    difficulty = TEST_DIFFICULTY_INDEX,
    lastQuestion = Some(TEST_EXPRESSION_POSITIVE_RESULT)
  )
  private val CORRECT_ANSWER1                 =
    logic.calculateResult(logic.getListFromExpression(TEST_EXPRESSION_POSITIVE_RESULT.text))
  private val WRONG_ANSWER1                   = CORRECT_ANSWER1 + 1
  private val TEST_EXPRESSION_NEGATIVE_RESULT = SimpleTextQuestion("7 - 4 * 2")
  private val logic2                          = logic.copy(lastQuestion = Some(TEST_EXPRESSION_NEGATIVE_RESULT))
  private val CORRECT_ANSWER2                 =
    logic.calculateResult(logic.getListFromExpression(TEST_EXPRESSION_NEGATIVE_RESULT.text))
  private val WRONG_ANSWER2                   = CORRECT_ANSWER2 + 1

  test(
    "The validator of the mini-game should return true for the correct answers (case positive results)"
  ) {
    logic.validateAnswer(CORRECT_ANSWER1) shouldBe true
  }

  test(
    "The validator of the mini-game should return false for the wrong answers (case positive results)"
  ) {
    logic.validateAnswer(WRONG_ANSWER1) shouldBe false
  }

  test(
    "The validator of the mini-game should return true for the correct answers (case negative results)"
  ) {
    logic2.validateAnswer(CORRECT_ANSWER2) shouldBe true
  }

  test(
    "The validator of the mini-game should return false for the wrong answers (case negative results)"
  ) {
    logic2.validateAnswer(WRONG_ANSWER2) shouldBe false
  }

  test(
    "The method calculate answer should be able to calculate expressions with positive result"
  ) {
    CORRECT_ANSWER1 shouldBe 16
  }

  test(
    "The method calculate answer should be able to calculate expressions with negative result"
  ) {
    CORRECT_ANSWER2 shouldBe -1
  }

  test(
    "The method generateQuestion should return a non-empty string and new logic with difficulty increased"
  ) {
    val (newLogic, question) = logic.generateQuestion
    question.text should not be empty
    newLogic.asInstanceOf[FastCalcLogic].difficulty shouldBe logic.difficulty + FAST_CALC_DIFFICULTY_STEP
  }

  test("The method isMiniGameFinished should return false until rounds are reached") {
    val (nextLogic, _) = logic.generateQuestion
    nextLogic.isMiniGameFinished shouldBe false
  }

  test("The method isMiniGameFinished should return true when rounds are finished") {
    val finishedLogic = FastCalcLogic(rounds = 1, currentRound = 1)
    finishedLogic.isMiniGameFinished shouldBe true
  }
