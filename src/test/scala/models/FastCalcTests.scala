package models

import org.scalatest.funsuite.AnyFunSuite
import utils.FastCalcConstants.FAST_CALC_DIFFICULTY_STEP

class FastCalcTests extends AnyFunSuite:
  private val TEST_DIFFICULTY_INDEX = 3
  private val TEST_EXPRESSION       = "8 + 4 * 2"
  private val logic                 = FastCalcLogic(
    rounds = 5,
    difficulty = TEST_DIFFICULTY_INDEX,
    lastQuestion = Some(TEST_EXPRESSION)
  )
  private val CORRECT_ANSWER        = logic.calculateResult(logic.getListFromExpression(TEST_EXPRESSION))
  private val WRONG_ANSWER          = CORRECT_ANSWER + 1

  test("The validator of the mini-game should return true for the correct answers") {
    assert(logic.validateAnswer(CORRECT_ANSWER))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!logic.validateAnswer(WRONG_ANSWER))
  }

  test(
    "The method generateQuestion should return a non-empty string and new logic with difficulty increased"
  ) {
    val (newLogic, question) = logic.generateQuestion
    println(s"Generated question: $question")
    assert(question.nonEmpty)
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
    val finishedLogic = FastCalcLogic(rounds = 1, currentRound = 1, difficulty = 1)
    assert(finishedLogic.isMiniGameFinished)
  }
