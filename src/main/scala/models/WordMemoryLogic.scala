package models

import scala.util.Random

case class WordMemoryLogic(
    rounds: Int,
    currentRound: Int,
    difficulty: Int,
    lastQuestion: Option[String]
) extends MultipleAnswersMiniGameLogic:
  private val words                 = Seq(
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
  private def hasNextRound: Boolean =
    currentRound < rounds

  override def generateQuestion: (MultipleAnswersMiniGameLogic, String) =
    if !hasNextRound then throw new IllegalStateException("No more rounds")
    else
      val wordsNumber = 3 + difficulty
      val newQuestion = Random.shuffle(words).take(wordsNumber).mkString(" ")
      (
        this.copy(rounds, currentRound + 1, difficulty + 1, lastQuestion = Some(newQuestion)),
        newQuestion
      )

  override def evaluateAnswers(question: String, answer: String): Double =
    val expectedWordsNumber = question.split(" ").toSet
    val answerWordsNumber   = answer.split(" ").filter(_.nonEmpty).toSet
    answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size
