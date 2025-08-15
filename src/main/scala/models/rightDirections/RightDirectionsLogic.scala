package models.rightDirections

import models.rightDirections.structure.Token
import models.MiniGameLogic
import models.rightDirections.structure.*
import utils.constants.RightDirectionsConstants.*
import utils.SimpleTextQuestion

import scala.annotation.tailrec

case class RightDirectionsLogic(
    rounds: Int,
    difficulty: Float = 0,
    lastQuestion: Option[SimpleTextQuestion] = None,
    currentRound: Int = 0
) extends MiniGameLogic[SimpleTextQuestion, String, Boolean]:

  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, String, Boolean], SimpleTextQuestion) =
    val question = SimpleTextQuestion(trimQuestion(generateOperation))
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def parseAnswer(answer: String): String =
    identity(answer)

  override def validateAnswer(answer: String): Boolean =
    val trimmedAnswer         = answer.toLowerCase.trim
    val correctAnswer         = EvaluateOperation.evaluateOperationFromString(lastQuestion.get.text, Seq())
    val answerAsToken: Token  = Token.fromString(trimmedAnswer)
    val noAnswerCase: Boolean = correctAnswer.isEmpty && answerAsToken.equals(Token.Empty)
    correctAnswer.contains(answerAsToken) || noAnswerCase

  override def isMiniGameFinished: Boolean =
    currentRound == rounds

  @tailrec
  private def generateOperation: String =
    val question       = DirectionsTreeBuilder
      .buildOperationFromComplexity(difficulty.toInt)
      .toString
    val containsAnswer = EvaluateOperation.evaluateOperationFromString(question, Seq()).nonEmpty
    if containsAnswer || CAN_GENERATE_WRONG_OPERATIONS then question else generateOperation

  private def trimQuestion(question: String): String =
    question
      .replaceAll("\\(", "")
      .replaceAll("\\)", "")
