package models

import org.scalatest.funsuite.AnyFunSuite
import utils.SimpleTextQuestion

class WordMemoryTests extends AnyFunSuite:
  private val TEST_EXPRESSION_EASY      = SimpleTextQuestion("cow rainbow cat pillow")
  private val TEST_EXPRESSION_DIFFICULT = SimpleTextQuestion("cow rainbow cat pillow dog")

  private val gameLogic =
    WordMemoryLogic(
      rounds = 3,
      lastQuestion = Some(TEST_EXPRESSION_EASY)
    )

  private val COMPLETE_ANSWER_SAME_ORDER      = "cow rainbow cat pillow"
  private val COMPLETE_ANSWER_DIFFERENT_ORDER = "cow rainbow cat pillow"
  private val PARTIAL_ANSWER_SAME_ORDER       = "cow"
  private val PARTIAL_ANSWER_DIFFERENT_ORDER  = "rainbow cow"
  private val WRONG_SPELL_ANSWER              = "cow rainbw cat pillow "
  private val WRONG_EMPTY_ANSWER              = ""

  test("The validator returns 1.0 for complete answer in same order") {
    val score = gameLogic.validateAnswer(COMPLETE_ANSWER_SAME_ORDER)
    assert(score == 1.0)
  }

  test("The validator returns 1.0 for complete answer in different order") {
    val score = gameLogic.validateAnswer(COMPLETE_ANSWER_DIFFERENT_ORDER)
    assert(score == 1.0)
  }

  test("The validator returns partial score for partial correct answers (same order)") {
    val score = gameLogic.validateAnswer(PARTIAL_ANSWER_SAME_ORDER)
    assert(score < 1.0)
  }

  test("The validator returns partial score for partial correct answers (different order)") {
    val score = gameLogic.validateAnswer(PARTIAL_ANSWER_DIFFERENT_ORDER)
    assert(score < 1.0)
  }

  test("The validator does not count wrong spelled answer") {
    val score = gameLogic.validateAnswer(WRONG_SPELL_ANSWER)
    assert(score < 1.0)
  }

  test("The validator returns 0.0 for empty answer") {
    val score = gameLogic.validateAnswer(WRONG_EMPTY_ANSWER)
    assert(score == 0.0)
  }

  test("generateQuestion should return a non-empty string") {
    assert(gameLogic.generateQuestion._2.text != WRONG_EMPTY_ANSWER)
  }

  test("generateQuestion should return more words as difficulty increases") {
    val (gameLogicCopy, easyQuestion) = gameLogic.generateQuestion
    val countEasy                     = easyQuestion.text.split(" ").length
    val countDifficult                = gameLogicCopy.generateQuestion._2.text.split(" ").length
    assert(countEasy < countDifficult)
  }
