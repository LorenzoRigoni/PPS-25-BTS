package models

import scala.util.Random
import utils.ColoredCountConstants.{
  COLORED_COUNT_DIFFICULTY_STEP,
  MAX_POSSIBLE_NUMBER,
  MIN_NUMBERS,
  MIN_POSSIBLE_NUMBER,
  MULT_DIFFICULTY
}
import utils.ColoredCountColors
import utils.ColoredCountQuestion

/**
 * This case class manages the logic of the Colored Count mini-game.
 */
case class ColoredCountLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[ColoredCountQuestion] = None
) extends MiniGameLogic[ColoredCountQuestion, Int, Boolean]:

  override def generateQuestion
      : (MiniGameLogic[ColoredCountQuestion, Int, Boolean], ColoredCountQuestion) =
    val totalNumbers  = MIN_NUMBERS + difficulty * MULT_DIFFICULTY
    val numbers       = Seq.fill(totalNumbers)(Random.between(MIN_POSSIBLE_NUMBER, MAX_POSSIBLE_NUMBER))
    val colorList     = Seq.fill(totalNumbers)(
      ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    )
    val zipped        = numbers.zip(colorList)
    val questionColor = ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    val question      = ColoredCountQuestion(zipped, questionColor)

    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + COLORED_COUNT_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def validateAnswer(answer: Int): Boolean =
    lastQuestion match
      case Some(q) => answer == q.numbersWithColor.count((_, c) => c == q.colorRequired)
      case _       => false

  override def isMiniGameFinished: Boolean = currentRound == rounds
