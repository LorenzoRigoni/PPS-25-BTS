package models

import scala.util.Random
import utils.constants.WordsForMiniGames.WORDS
import utils.SimpleTextQuestion

/**
 * This case class manage the logic of the mini-game "Count Words"
 * @param rounds
 *   The total number of rounds
 * @param currentRound
 *   The current round
 * @param difficulty
 *   The current difficulty
 * @param lastQuestion
 *   The last question generated
 */
case class CountWordsLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[SimpleTextQuestion] = None
) extends MiniGameLogic[SimpleTextQuestion, Int, Boolean]:
  private val COUNT_WORDS_DIFFICULTY_STEP = 1
  private val MIN_NUMBER_WORDS            = 3
  private val MIN_DIFFICULTY              = 2

  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
    val minRand        = math.max(1, difficulty - 1)
    val numOfWords     =
      if difficulty <= MIN_DIFFICULTY then MIN_NUMBER_WORDS + Random.between(0, difficulty + 1)
      else MIN_NUMBER_WORDS + Random.between(minRand, difficulty + 1)
    val wordsGenerated = Seq.fill(numOfWords)(WORDS(Random.nextInt(WORDS.size))).mkString(" ")
    val question       = SimpleTextQuestion(wordsGenerated)
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + COUNT_WORDS_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def parseAnswer(answer: String): Option[Int] = answer.trim.toIntOption

  override def validateAnswer(answer: Int): Boolean =
    lastQuestion match
      case Some(q) => answer == q.text.split("\\s+").count(_.nonEmpty)
      case _       => false

  override def isMiniGameFinished: Boolean = currentRound == rounds
