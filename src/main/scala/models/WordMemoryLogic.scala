package models

import scala.util.Random
import utils.WordsForMiniGames.WORDS
import utils.SimpleTextQuestion
import utils.WordMemoryConstants.MIN_WORD_NUM

/**
 * This case class manages the logic of the Word Memory mini-game.
 */
case class WordMemoryLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[SimpleTextQuestion] = None
) extends MiniGameLogic[SimpleTextQuestion, String, Double]:
  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, String, Double], SimpleTextQuestion) =

    val wordsNumber    = MIN_WORD_NUM + difficulty
    val wordsGenerated = Random.shuffle(WORDS).take(wordsNumber).mkString(" ")
    val question       = SimpleTextQuestion(wordsGenerated)
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + 1,
        lastQuestion = Some(question)
      ),
      question
    )

  extension (s: String) private def toWordSet: Set[String] = s.split(" ").filter(_.nonEmpty).toSet

  override def parseAnswer(answer: String): String =
    identity(answer)

  override def validateAnswer(answer: String): Double =
    lastQuestion.fold(0.0)(question =>
      val expectedWordsNumber = question.text.toWordSet
      val answerWordsNumber   = answer.toWordSet
      answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size
    )

  override def isMiniGameFinished: Boolean = currentRound == rounds
