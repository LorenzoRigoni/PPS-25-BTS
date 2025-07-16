import models.FastCalcLogic
import org.scalatest.funsuite.AnyFunSuite

class FastCalcTests extends AnyFunSuite:
  private val TEST_EXPRESSION = "8 + (4 x 2)"
  private val CORRECT_ANSWER = 16
  private val WRONG_ANSWER = 17 
  
  test("The validator of the mini-game should return true for the correct answers") {
    assert(FastCalcLogic.validateAnswer(TEST_EXPRESSION, CORRECT_ANSWER))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!FastCalcLogic.validateAnswer(TEST_EXPRESSION, WRONG_ANSWER))
  }
