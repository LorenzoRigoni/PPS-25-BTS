package models

import scala.util.Random

/**
 * This object manage the logic of the Count Words mini-game.
 */
object CountWordsLogic extends MiniGameLogic:
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
  private val minNumOfWords = 3

  override def generateQuestion(difficult: Int): String =
    val minimumRandom = difficult - 2
    val numOfWords =
      if difficult <= 2 then minNumOfWords + Random.nextInt(difficult)
      else minNumOfWords + (minimumRandom + Random.nextInt((difficult - minimumRandom) + 1))
    Seq.fill(numOfWords)(words(Random.nextInt(words.size))).mkString(" ")

  override def validateAnswer[Int](question: String, answer: Int): Boolean =
    answer == question.split("\\s+").count(_.nonEmpty)
