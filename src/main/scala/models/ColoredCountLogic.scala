package models

import scala.util.Random

object ColoredCountLogic extends MiniGameLogic:
  private val BASE_NUMBER = 3
  private val colors      = List("RED", "YELLOW", "BLUE", "BLACK")

  override def generateQuestion: String =
    val totalNumbers = BASE_NUMBER /*+ difficultyLevel * 2*/
    val numbers      = List.fill(totalNumbers)(Random.between(1, 10))
    val colorList    = List.fill(totalNumbers)(colors(Random.nextInt(colors.length)))
    val zipped       = numbers.zip(colorList)

    val questionColor = colors(Random.nextInt(colors.length))
    val numbersPart   = zipped.map((n, c) => s"$n:$c").mkString(" ")

    s"$numbersPart | $questionColor"

  override def validateAnswer[Int](question: String, answer: Int): Boolean =
    val questionParts     = question.split("\\|").map(_.trim)
    val coloredNumberPart = questionParts(0).split(" ").toList
    val targetColor       = questionParts(1)

    answer == coloredNumberPart.count(_.split(':')(1) == targetColor)

  override def isMiniGameFinished: Boolean = ???
  