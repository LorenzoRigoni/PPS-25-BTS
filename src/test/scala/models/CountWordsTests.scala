package models

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import utils.constants.CountWordsConstants.COUNT_WORDS_TURNS

/**
 * This class tests the logic of the mini-game Count Words.
 */
class CountWordsTests extends AnyFunSuite with Matchers:
  private val TEST_SENTENCE        = SimpleTextQuestion("A test sentence")
  private val CORRECT_NUM_OF_WORDS = getNumOfWords(TEST_SENTENCE.text)
  private val WRONG_NUM_OF_WORDS   = CORRECT_NUM_OF_WORDS + 1
  private val countWordsLogic      = CountWordsLogic(
    COUNT_WORDS_TURNS,
    lastQuestion = Some(TEST_SENTENCE)
  )

  private def getNumOfWords(sentence: String): Int =
    sentence.split("\\s+").count(_.nonEmpty)

  test("The validator of the mini-game should return true for the correct answers") {
    countWordsLogic.validateAnswer(CORRECT_NUM_OF_WORDS) shouldBe true
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    countWordsLogic.validateAnswer(WRONG_NUM_OF_WORDS) shouldBe false
  }

  test("The generator of the mini-game should generate questions based on difficulty") {
    val (_, question) = countWordsLogic.generateQuestion
    assert(getNumOfWords(countWordsLogic.lastQuestion.get.text) <= getNumOfWords(question.text))
  }

  test("The mini-game should end when it reaches the maximum number of turns") {
    val lastMiniGameLogic = countWordsLogic.copy(
      currentRound = COUNT_WORDS_TURNS
    )
    lastMiniGameLogic.isMiniGameFinished shouldBe true
  }
