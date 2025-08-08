package models

import scala.util.Random
import utils.ColoredCountConstants.{COLORS, MIN_NUMBERS, COLORED_COUNT_DIFFICULTY_STEP}

case class ColoredCountLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[String] = None
) extends MiniGameLogic[Int, Boolean]:

  override def generateQuestion: (MiniGameLogic[Int, Boolean], String) =
    val totalNumbers = MIN_NUMBERS + difficulty * 2
    val numbers      = List.fill(totalNumbers)(Random.between(1, 10))
    val colorList    = List.fill(totalNumbers)(COLORS(Random.nextInt(COLORS.length)))
    val zipped       = numbers.zip(colorList)

    val questionColor = COLORS(Random.nextInt(COLORS.length))
    val numbersPart   = zipped.map((n, c) => s"$n:$c").mkString(" ")
    val question      = s"$numbersPart | $questionColor"

    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + COLORED_COUNT_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def validateAnswer(answer: Int): Boolean =
    val questionParts     = lastQuestion.get.split("\\|").map(_.trim)
    val coloredNumberPart = questionParts(0).split(" ").toList
    val targetColor       = questionParts(1)

    answer == coloredNumberPart.count(_.split(':')(1) == targetColor)

  override def isMiniGameFinished: Boolean = currentRound == rounds
