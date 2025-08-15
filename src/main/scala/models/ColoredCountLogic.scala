package models

import scala.util.Random
import utils.ColoredCountColors
import utils.ColoredCountQuestion

/**
 * This case class manage the logic of the mini-game "Colored Count"
 * @param rounds The total number of rounds
 * @param currentRound The current round
 * @param difficulty The current difficulty
 * @param lastQuestion The last question generated
 */
case class ColoredCountLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[ColoredCountQuestion] = None
) extends MiniGameLogic[ColoredCountQuestion, Int, Boolean]:
  private val MIN_NUMBERS                   = 3
  private val COLORED_COUNT_DIFFICULTY_STEP = 1
  private val MULT_DIFFICULTY               = 2
  private val MIN_POSSIBLE_NUMBER           = 1
  private val MAX_POSSIBLE_NUMBER           = 10

  override def generateQuestion
      : (MiniGameLogic[ColoredCountQuestion, Int, Boolean], ColoredCountQuestion) =
    val totalNumbers  = MIN_NUMBERS + difficulty * MULT_DIFFICULTY
    val numbers       = Seq.fill(totalNumbers)(Random.between(MIN_POSSIBLE_NUMBER, MAX_POSSIBLE_NUMBER))
    val colorList     = Seq.fill(totalNumbers)(
      ColoredCountColors.values(Random.nextInt(ColoredCountColors.values.length))
    )
    val zipped        = numbers zip colorList
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

  override def parseAnswer(answer: String): Int =
    answer.trim.toIntOption.getOrElse(
      throw IllegalArgumentException(s"$answer is not an Int")
    )

  override def validateAnswer(answer: Int): Boolean =
    lastQuestion match
      case Some(q) => answer == q.numbersWithColor.count((_, c) => c == q.colorRequired)
      case _       => false

  override def isMiniGameFinished: Boolean = currentRound == rounds
