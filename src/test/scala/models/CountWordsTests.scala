package models

import org.scalatest.funsuite.AnyFunSuite
import utils.CountWordsConstants.COUNT_WORDS_TURNS
import utils.SimpleTextQuestion

/**
 * This class tests the logic of the mini-game Count Words.
 */
class CountWordsTests extends AnyFunSuite:
  private val TEST_SENTENCE        = SimpleTextQuestion("A test sentence")
  private val CORRECT_NUM_OF_WORDS = getNumOfWords(TEST_SENTENCE.text)
  private val WRONG_NUM_OF_WORDS   = CORRECT_NUM_OF_WORDS + 1

  private val countWordsLogic = CountWordsLogic(
    COUNT_WORDS_TURNS,
    lastQuestion = Some(TEST_SENTENCE)
  )

  private def getNumOfWords(sentence: String): Int =
    sentence.split("\\s+").count(_.nonEmpty)

  test("The validator of the mini-game should return true for the correct answers") {
    assert(countWordsLogic.validateAnswer(CORRECT_NUM_OF_WORDS))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!countWordsLogic.validateAnswer(WRONG_NUM_OF_WORDS))
  }

  test("The generator of the mini-game should generate questions based on difficulty") {
    val (_, question) = countWordsLogic.generateQuestion
    assert(getNumOfWords(countWordsLogic.lastQuestion.get.text) <= getNumOfWords(question.text))
  }

  test("The mini-game should end when it reaches the maximum number of turns") {
    val lastMiniGameLogic = countWordsLogic.copy(
      currentRound = COUNT_WORDS_TURNS
    )
    assert(lastMiniGameLogic.isMiniGameFinished)
  }
