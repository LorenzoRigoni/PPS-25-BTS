package models

import scala.util.Random
import utils.CountWordsConstants.{MIN_NUMBER_WORDS, COUNT_WORDS_DIFFICULTY_STEP}
import utils.WordsForMiniGames.WORDS
import utils.SimpleTextQuestion

/**
 * This case class manages the logic of the Count Words mini-game.
 */
case class CountWordsLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[SimpleTextQuestion] = None
) extends MiniGameLogic[SimpleTextQuestion, Int, Boolean]:

  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
    val minRand        = math.max(1, difficulty - 1)
    val numOfWords     =
      if difficulty <= 2 then MIN_NUMBER_WORDS + Random.between(0, difficulty + 1)
      else MIN_NUMBER_WORDS + Random.between(minRand, difficulty + 1)
    val wordsGenerated = Seq.fill(numOfWords)(WORDS(Random.nextInt(WORDS.size))).mkString(" ")
    val question       = SimpleTextQuestion(wordsGenerated)

    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + COUNT_WORDS_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def validateAnswer(answer: Int): Boolean =
    answer == lastQuestion.get.text.split("\\s+").count(_.nonEmpty)

  override def isMiniGameFinished: Boolean = currentRound == rounds
