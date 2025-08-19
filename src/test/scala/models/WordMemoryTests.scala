package models

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe

/**
 * This class tests the logic of the mini-game Word Memory.
 */
class WordMemoryTests extends AnyFunSuite with Matchers:
  private val TEST_EXPRESSION_EASY            = SimpleTextQuestion("cow rainbow cat pillow")
  private val TEST_EXPRESSION_DIFFICULT       = SimpleTextQuestion("cow rainbow cat pillow dog")
  private val COMPLETE_ANSWER_SAME_ORDER      = "cow rainbow cat pillow"
  private val COMPLETE_ANSWER_DIFFERENT_ORDER = "cow rainbow cat pillow"
  private val PARTIAL_ANSWER_SAME_ORDER       = "cow"
  private val PARTIAL_ANSWER_DIFFERENT_ORDER  = "rainbow cow"
  private val WRONG_SPELL_ANSWER              = "cow rainbw cat pillow "
  private val gameLogic                       =
    WordMemoryLogic(
      rounds = 3,
      lastQuestion = Some(TEST_EXPRESSION_EASY)
    )

  test("The validator returns 1.0 for complete answer in same order") {
    val score = gameLogic.validateAnswer(COMPLETE_ANSWER_SAME_ORDER)
    score shouldBe 1.0
  }

  test("The validator returns 1.0 for complete answer in different order") {
    val score = gameLogic.validateAnswer(COMPLETE_ANSWER_DIFFERENT_ORDER)
    score shouldBe 1.0
  }

  test("The validator returns partial score for partial correct answers (same order)") {
    val score = gameLogic.validateAnswer(PARTIAL_ANSWER_SAME_ORDER)
    score should be < 1.0
  }

  test("The validator returns partial score for partial correct answers (different order)") {
    val score = gameLogic.validateAnswer(PARTIAL_ANSWER_DIFFERENT_ORDER)
    score should be < 1.0
  }

  test("The validator does not count wrong spelled answer") {
    val score = gameLogic.validateAnswer(WRONG_SPELL_ANSWER)
    score should be < 1.0
  }

  test("The validator returns 0.0 for empty answer") {
    val score = gameLogic.validateAnswer("")
    score shouldBe 0.0
  }

  test("generateQuestion should return a non-empty string") {
    gameLogic.generateQuestion._2.text should not be empty
  }

  test("generateQuestion should return more words as difficulty increases") {
    val (gameLogicCopy, easyQuestion) = gameLogic.generateQuestion
    val countEasy                     = easyQuestion.text.split(" ").length
    val countDifficult                = gameLogicCopy.generateQuestion._2.text.split(" ").length
    countEasy should be < countDifficult
  }
