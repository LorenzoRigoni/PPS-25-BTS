import models.CountWordsLogic
import org.scalatest.funsuite.AnyFunSuite

class CountWordsTests extends AnyFunSuite:
  private val TEST_SENTENCE = "This is a test sentence"
  private val CORRECT_NUM_OF_WORDS = TEST_SENTENCE.split("\\s+").count(_.nonEmpty)
  private val WRONG_NUM_OF_WORDS = CORRECT_NUM_OF_WORDS + 1

  test("The validator of the mini-game should return true for the correct answers") {
    assert(CountWordsLogic.validateAnswer(TEST_SENTENCE, CORRECT_NUM_OF_WORDS))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!CountWordsLogic.validateAnswer(TEST_SENTENCE, WRONG_NUM_OF_WORDS))
  }