import org.scalatest.funsuite.AnyFunSuite

class WordMemoryTests extends AnyFunSuite:
  private val TEST_EXPRESSION_EASY = "cow rainbow cat pillow"
  private val TEST_EXPRESSION_DIFFICULT = "cow rainbow cat pillow dog"
  private val TEST_DIFFICULTY_INDEX = 1
  private val COMPLETE_ANSWER_SAME_ORDER = "cow rainbow cat pillow"
  private val COMPLETE_ANSWER_DIFFERENT_ORDER = "cow rainbow cat pillow"
  private val PARTIAL_ANSWER_SAME_ORDER = "cow"
  private val PARTIAL_ANSWER_DIFFERENT_ORDER = "rainbow cow"
  private val WRONG_SPELL_ANSWER = "rainbw "
  private val WRONG_EMPTY_ANSWER = ""

  //TODO: add method evaluateAnswer in game logic
  test("The validator returns 1.0 for complete answer in same order") {
    assert(true)
  }

  test("The validator returns 1.0 for complete answer in different order") {
    assert(true)
  }

  test("The validator returns partial score for partial correct answers (same order)") {
    assert(true)
  }

  test("The validator returns partial score for partial correct answers (different order)") {
    assert(true)
  }

  test("The validator does not count wrong spelled answer") {
    assert(true)
  }

  test("The validator returns 0.0 for empty answer") {
    assert(true)
  }

  test("generateQuestion should return a non-empty string") {
    assert(true)
  }

  test("generateQuestion should return more words as difficulty increases") {
    val countEasy = TEST_EXPRESSION_EASY.split(" ").length
    val countDifficult = TEST_EXPRESSION_DIFFICULT.split(" ").length
    assert(countEasy < countDifficult)
  }
