package models

import org.scalatest.funsuite.AnyFunSuite
import utils.ColoredCountConstants.{COLORED_COUNT_TURNS, COLORS}

/**
 * This class tests the logic of the mini-game Colored Count.
 */
class ColoredCountTests extends AnyFunSuite:
  private val TEST_QUESTION = generateTestQuestion._1
  private val RIGHT_ANSWER  = generateTestQuestion._2
  private val WRONG_ANSWER  = RIGHT_ANSWER + 1

  private val coloredCountLogic = ColoredCountLogic(
    COLORED_COUNT_TURNS,
    lastQuestion = Some(TEST_QUESTION)
  )

  private def generateTestQuestion: (String, Int) =
    val numbers       = List(1, 2, 3, 4)
    val colors        = COLORS
    val colorRequired = COLORS(1)
    val question      = numbers.zip(colors).map((n, c) => s"$n:$c").mkString(" ") + "|" + colorRequired
    (question, colors.count(_ == colorRequired))

  private def getCountOfNumbers(question: String): Int =
    val parts = question.split("\\|").map(_.trim)
    parts(0).split(" ").map(_.trim).length

  test("The validator of the mini-game should return true for the correct answers") {
    assert(coloredCountLogic.validateAnswer(RIGHT_ANSWER))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!coloredCountLogic.validateAnswer(WRONG_ANSWER))
  }

  test("The question generator should increase the numbers with the difficulty") {
    val (_, question) = coloredCountLogic.generateQuestion
    assert(getCountOfNumbers(coloredCountLogic.lastQuestion.get) <= getCountOfNumbers(question))
  }
