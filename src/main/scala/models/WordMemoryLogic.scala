package models

import scala.util.Random
import utils.WordsForMiniGames.WORDS
import utils.SimpleTextQuestion

case class WordMemoryLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[SimpleTextQuestion] = None
) extends MiniGameLogic[SimpleTextQuestion, String, Double]:

  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, String, Double], SimpleTextQuestion) =
    if isMiniGameFinished then throw new IllegalStateException("No more rounds")
    else
      val wordsNumber    = 3 + difficulty
      val wordsGenerated = Random.shuffle(WORDS).take(wordsNumber).mkString(" ")
      val question       = SimpleTextQuestion(wordsGenerated)
      (
        this.copy(rounds, currentRound + 1, difficulty + 1, lastQuestion = Some(question)),
        question
      )

  override def validateAnswer(answer: String): Double =
    val expectedWordsNumber = lastQuestion.get.text.split(" ").toSet
    val answerWordsNumber   = answer.split(" ").filter(_.nonEmpty).toSet
    answerWordsNumber.count(expectedWordsNumber.contains).toDouble / expectedWordsNumber.size

  override def isMiniGameFinished: Boolean = currentRound == rounds
