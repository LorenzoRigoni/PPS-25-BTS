package models

import scala.util.Random
import utils.WordsForMiniGames.WORDS

/**
 * This case class manages the logic of the Word Memory mini-game.
 */
case class WordMemoryLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[String] = None
) extends MiniGameLogic[String, Double]:

  override def generateQuestion: (MiniGameLogic[String, Double], String) =
    val wordsNumber = 3 + difficulty
    val newQuestion = Random.shuffle(WORDS).take(wordsNumber).mkString(" ")
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + 1,
        lastQuestion = Some(newQuestion)
      ),
      newQuestion
    )

  extension (s: String) private def toWordSet: Set[String] = s.split(" ").filter(_.nonEmpty).toSet

  override def validateAnswer(answer: String): Double =
    lastQuestion.fold(0.0)(question =>
      val expectedWordsNumber = question.toWordSet
      val answerWordsNumber   = answer.toWordSet
      answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size
    )

  override def isMiniGameFinished: Boolean = currentRound == rounds
