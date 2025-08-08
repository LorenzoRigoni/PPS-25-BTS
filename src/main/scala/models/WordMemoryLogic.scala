package models

import scala.util.Random
import utils.WordsForMiniGames.WORDS

case class WordMemoryLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[String] = None
) extends MiniGameLogic[String, Double]:

  override def generateQuestion: (MiniGameLogic[String, Double], String) =
    if isMiniGameFinished then throw new IllegalStateException("No more rounds")
    else
      val wordsNumber = 3 + difficulty
      val newQuestion = Random.shuffle(WORDS).take(wordsNumber).mkString(" ")
      (
        this.copy(rounds, currentRound + 1, difficulty + 1, lastQuestion = Some(newQuestion)),
        newQuestion
      )

  override def validateAnswer(answer: String): Double =
    val expectedWordsNumber = lastQuestion.get.split(" ").toSet
    val answerWordsNumber   = answer.split(" ").filter(_.nonEmpty).toSet
    answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size

  override def isMiniGameFinished: Boolean = currentRound == rounds
