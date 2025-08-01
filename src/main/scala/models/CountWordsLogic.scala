package models

import scala.util.Random
import utils.CountWordsConstants.{MIN_NUMBER_WORDS, DIFFICULTY_STEP}

/**
 * This object manage the logic of the Count Words mini-game.
 */
case class CountWordsLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[String] = None
) extends MiniGameLogic:
  private val words         = Seq(
    "apple",
    "bridge",
    "cloud",
    "dream",
    "forest",
    "guitar",
    "house",
    "island",
    "jungle",
    "kangaroo",
    "light",
    "mountain",
    "notebook",
    "ocean",
    "puzzle",
    "quick",
    "river",
    "stone",
    "train",
    "umbrella",
    "village",
    "window",
    "yellow",
    "zebra",
    "adventure",
    "balance",
    "circle",
    "dance",
    "energy",
    "future"
  )
  private val minNumOfWords = MIN_NUMBER_WORDS

  override def generateQuestion: (MiniGameLogic, String) =
    val minRand    = math.max(1, difficulty - 1)
    val numOfWords =
      if difficulty <= 2 then minNumOfWords + Random.between(0, difficulty + 1)
      else minNumOfWords + Random.between(minRand, difficulty + 1)
    val question   = Seq.fill(numOfWords)(words(Random.nextInt(words.size))).mkString(" ")

    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def validateAnswer[Int](answer: Int): Boolean =
    answer == lastQuestion.get.split("\\s+").count(_.nonEmpty)

  override def isMiniGameFinished: Boolean = currentRound >= rounds
