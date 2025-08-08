package models

import org.scalatest.funsuite.AnyFunSuite
import utils.CountWordsConstants.{COUNT_WORDS_TURNS, COUNT_WORDS_DIFFICULTY_STEP}

class CountWordsTests extends AnyFunSuite:
  private val TEST_SENTENCE        = "This is a test sentence"
  private val countWordsLogic      = CountWordsLogic(
    COUNT_WORDS_TURNS,
    lastQuestion = Some(TEST_SENTENCE)
  )
  private val CORRECT_NUM_OF_WORDS = getNumOfWords(TEST_SENTENCE)
  private val WRONG_NUM_OF_WORDS   = CORRECT_NUM_OF_WORDS + 1
  private val TEST_DIFFICULT       = 3

  private def getNumOfWords(sentence: String): Int =
    sentence.split("\\s+").count(_.nonEmpty)

  test("The validator of the mini-game should return true for the correct answers") {
    assert(countWordsLogic.validateAnswer(CORRECT_NUM_OF_WORDS))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!countWordsLogic.validateAnswer(WRONG_NUM_OF_WORDS))
  }

  test("The generator of the mini-game should generate questions based on difficulty") {
    assert(getNumOfWords(countWordsLogic.generateQuestion._2) >= TEST_DIFFICULT)
  }
