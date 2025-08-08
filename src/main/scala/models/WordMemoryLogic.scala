package models

import scala.util.Random

case class WordMemoryLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[String] = None
) extends MiniGameLogic[String, Double]:
  private val words = Seq(
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

  override def generateQuestion: (MiniGameLogic[String, Double], String) =
    if isMiniGameFinished then throw new IllegalStateException("No more rounds")
    else
      val wordsNumber = 3 + difficulty
      val newQuestion = Random.shuffle(words).take(wordsNumber).mkString(" ")
      (
        this.copy(rounds, currentRound + 1, difficulty + 1, lastQuestion = Some(newQuestion)),
        newQuestion
      )

  override def validateAnswer(answer: String): Double =
    val expectedWordsNumber = lastQuestion.get.split(" ").toSet
    val answerWordsNumber   = answer.split(" ").filter(_.nonEmpty).toSet
    answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size

  override def isMiniGameFinished: Boolean = currentRound == rounds
