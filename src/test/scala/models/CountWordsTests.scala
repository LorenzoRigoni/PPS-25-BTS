package models

import org.scalatest.funsuite.AnyFunSuite
import utils.CountWordsConstants.{AGE_TEST_TURNS, DIFFICULTY_STEP}

//TODO: refactor CountWords Tests
class CountWordsTests extends AnyFunSuite:
  private val countWordsLogic      = CountWordsLogic(AGE_TEST_TURNS, DIFFICULTY_STEP)
  private val TEST_SENTENCE        = "This is a test sentence"
  private val CORRECT_NUM_OF_WORDS = getNumOfWords(TEST_SENTENCE)
  private val WRONG_NUM_OF_WORDS   = CORRECT_NUM_OF_WORDS + 1
  private val TEST_DIFFICULT       = 4

  private def getNumOfWords(sentence: String): Int =
    sentence.split("\\s+").count(_.nonEmpty)

  test("The validator of the mini-game should return true for the correct answers") {
    assert(countWordsLogic.validateAnswer(TEST_SENTENCE, CORRECT_NUM_OF_WORDS))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!countWordsLogic.validateAnswer(TEST_SENTENCE, WRONG_NUM_OF_WORDS))
  }

  test("The generator of the mini-game should generate questions based on difficulty") {
    // assert(getNumOfWords(countWordsLogic.generateQuestion) >= TEST_DIFFICULT)
    assert(false)
  }
