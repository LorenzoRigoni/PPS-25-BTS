package models

import org.scalatest.funsuite.AnyFunSuite
import utils.constants.FastCalcConstants.{FAST_CALC_DIFFICULTY_STEP, FAST_CALC_TURNS}
import utils.SimpleTextQuestion

class FastCalcTests extends AnyFunSuite:
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
    assert(logic.validateAnswer(CORRECT_ANSWER1))
  }

  test(
    "The validator of the mini-game should return false for the wrong answers (case positive results)"
  ) {
    assert(!logic.validateAnswer(WRONG_ANSWER1))
  }

  test(
    "The validator of the mini-game should return true for the correct answers (case negative results)"
  ) {
    assert(logic2.validateAnswer(CORRECT_ANSWER2))
  }

  test(
    "The validator of the mini-game should return false for the wrong answers (case negative results)"
  ) {
    assert(!logic2.validateAnswer(WRONG_ANSWER2))
  }

  test(
    "The method calculate answer should be able to calculate expressions with positive result"
  ) {
    assert(CORRECT_ANSWER1 == 16)
  }

  test(
    "The method calculate answer should be able to calculate expressions with negative result"
  ) {
    assert(CORRECT_ANSWER2 == -1)
  }

  test(
    "The method generateQuestion should return a non-empty string and new logic with difficulty increased"
  ) {
    val (newLogic, question) = logic.generateQuestion
    assert(question.text.nonEmpty)
    assert(
      newLogic
        .asInstanceOf[FastCalcLogic]
        .difficulty == logic.difficulty + FAST_CALC_DIFFICULTY_STEP
    )
  }

  test("The method isMiniGameFinished should return false until rounds are reached") {
    val (nextLogic, _) = logic.generateQuestion
    assert(!nextLogic.isMiniGameFinished)
  }

  test("The method isMiniGameFinished should return true when rounds are finished") {
    val finishedLogic = FastCalcLogic(rounds = 1, currentRound = 1)
    assert(finishedLogic.isMiniGameFinished)
  }
