package models

import scala.util.Random
import utils.CountWordsConstants.{MIN_NUMBER_WORDS, DIFFICULTY_STEP}

/**
 * This object manage the logic of the Count Words mini-game.
 */
case class CountWordsLogic(
    turns: Int,
    difficultyStep: Int = DIFFICULTY_STEP
) extends MiniGameLogic:
  private val words             = Seq(
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
  private val minNumOfWords     = MIN_NUMBER_WORDS
  private var currentTurn       = 0
  private var currentDifficulty = 1

  override def generateQuestion: (MiniGameLogic, String) =
    currentTurn += 1
    currentDifficulty += difficultyStep

    val minRand    = math.max(1, currentDifficulty - 1)
    val numOfWords =
      if currentDifficulty <= 2 then minNumOfWords + Random.between(0, currentDifficulty + 1)
      else minNumOfWords + Random.between(minRand, currentDifficulty + 1)

    (
      this.copy(), // TODO: Increase difficulty here
      Seq.fill(numOfWords)(words(Random.nextInt(words.size))).mkString(" ")
    )

  override def validateAnswer[Int](question: String, answer: Int): Boolean =
    answer == question.split("\\s+").count(_.nonEmpty)

  override def isMiniGameFinished: Boolean = currentTurn >= turns
