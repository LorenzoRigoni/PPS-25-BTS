import models.FastCalcLogic
import models.FastCalcLogic.getListFromExpression
import org.scalatest.funsuite.AnyFunSuite

class FastCalcTests extends AnyFunSuite:
  private val TEST_EXPRESSION = "8 + 4 * 2"
  private val TEST_DIFFICULTY_INDEX = 6
  private val CORRECT_ANSWER = FastCalcLogic.calculateResult(getListFromExpression(TEST_EXPRESSION))
  private val WRONG_ANSWER = FastCalcLogic.calculateResult(getListFromExpression(TEST_EXPRESSION)) + 1

  test("The validator of the mini-game should return true for the correct answers") {
    assert(FastCalcLogic.validateAnswer(TEST_EXPRESSION, CORRECT_ANSWER))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!FastCalcLogic.validateAnswer(TEST_EXPRESSION, WRONG_ANSWER))
  }

  test("generateQuestion should return a non-empty string") {
    val question = FastCalcLogic.generateQuestion(TEST_DIFFICULTY_INDEX)
    println(s"Generated question: $question")
    assert(question.nonEmpty)
  }
