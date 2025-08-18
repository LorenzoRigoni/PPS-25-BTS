package models

import scala.util.Random
import utils.constants.WordsForMiniGames.WORDS
import utils.SimpleTextQuestion
import utils.constants.WordMemoryConstants.MIN_WORD_NUM

/**
 * This case class manage the logic of the mini-game "Word Memory"
 * @param rounds
 *   The total number of rounds
 * @param currentRound
 *   The current round
 * @param difficulty
 *   The current difficulty
 * @param lastQuestion
 *   The last question generated
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

  override def parseAnswer(answer: String): Option[String] = Some(identity(answer))

  override def validateAnswer(answer: String): Double =
    lastQuestion.fold(0.0)(question =>
      val expectedWordsNumber = question.text.toWordSet
      val answerWordsNumber   = answer.toWordSet
      answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size
    )

  override def isMiniGameFinished: Boolean = currentRound == rounds
