package models

import org.scalatest.funsuite.AnyFunSuite
import utils.ColoredCountConstants.COLORED_COUNT_TURNS
import utils.{ColoredCountColors, ColoredCountQuestion}

/**
 * This class tests the logic of the mini-game Colored Count.
 */
class ColoredCountTests extends AnyFunSuite:
  private val NUMBERS       = Seq(1, 2, 3, 4)
  private val TEST_QUESTION = generateTestQuestion._1
  private val RIGHT_ANSWER  = generateTestQuestion._2
  private val WRONG_ANSWER  = RIGHT_ANSWER + 1

  private val coloredCountLogic = ColoredCountLogic(
    COLORED_COUNT_TURNS,
    lastQuestion = Some(TEST_QUESTION)
  )

  private def generateTestQuestion: (ColoredCountQuestion, Int) =
    val colors        = ColoredCountColors.values
    val colorRequired = colors(1)
    val question      = NUMBERS.zip(colors)
    (ColoredCountQuestion(question, colorRequired), colors.count(_ == colorRequired))

  test("The validator of the mini-game should return true for the correct answers") {
    assert(coloredCountLogic.validateAnswer(RIGHT_ANSWER))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!coloredCountLogic.validateAnswer(WRONG_ANSWER))
  }

  test("The question generator should increase the numbers with the difficulty") {
    val (_, question) = coloredCountLogic.generateQuestion
    assert(
      coloredCountLogic.lastQuestion.get.numbersWithColor.size <= question.numbersWithColor.size
    )
  }

  test("The mini-game should end when it reaches the maximum number of turns") {
    val lastMiniGameLogic = coloredCountLogic.copy(
      currentRound = COLORED_COUNT_TURNS
    )
    assert(lastMiniGameLogic.isMiniGameFinished)
  }
