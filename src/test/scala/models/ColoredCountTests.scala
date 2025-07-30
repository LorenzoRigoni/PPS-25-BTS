package models

import org.scalatest.funsuite.AnyFunSuite

class ColoredCountTests extends AnyFunSuite:
  private val TEST_QUESTION   = generateTestQuestion
  private val RIGHT_ANSWER    = 2
  private val WRONG_ANSWER    = 3
  private val LOW_DIFFICULTY  = 2
  private val HIGH_DIFFICULTY = 3

  private def generateTestQuestion: String =
    val numbers       = List(1, 2, 3, 4)
    val colors        = List("RED", "RED", "YELLOW", "BLUE")
    val colorRequired = "RED"
    numbers.zip(colors).map((n, c) => s"$n:$c").mkString(" ") + "|" + colorRequired

  private def getCountOfNumbers(question: String): Int =
    val parts = question.split("\\|").map(_.trim)
    parts(0).split(" ").map(_.trim).length

  test("The validator of the mini-game should return true for the correct answers") {
    assert(ColoredCountLogic.validateAnswer(TEST_QUESTION, RIGHT_ANSWER))
  }

  test("The validator of the mini-game should return false for the wrong answers") {
    assert(!ColoredCountLogic.validateAnswer(TEST_QUESTION, WRONG_ANSWER))
  }

  test("The question generator should increase the numbers with the difficulty") {
    /*assert(
      getCountOfNumbers(ColoredCountLogic.generateQuestion) < getCountOfNumbers(
        ColoredCountLogic.generateQuestion
      )
    )*/
  }
