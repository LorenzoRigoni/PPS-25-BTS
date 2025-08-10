package models.rightDirections

import models.rightDirections.structure.Symbol
import models.MiniGameLogic
import models.rightDirections.structure.{EvaluateOperation, SyntaxTreeBuilder}
import utils.RightDirectionsConstants.*
import utils.SimpleTextQuestion

import scala.annotation.tailrec

case class RightDirectionsLogic(
    rounds: Int,
    difficulty: Float = 0,
    lastQuestion: Option[SimpleTextQuestion] = None,
    currentRound: Int = 0
) extends MiniGameLogic[SimpleTextQuestion, String, Boolean]:

  override def generateQuestion: (MiniGameLogic[SimpleTextQuestion, String, Boolean], SimpleTextQuestion) =
    val question = SimpleTextQuestion(trimQuestion(generateOperation))
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def validateAnswer(answer: String): Boolean =
    val trimmedAnswer                  = answer.toLowerCase.trim
    val correctAnswer                  = EvaluateOperation.evaluateOperationFromString(lastQuestion.get.text, List())
    val answerAsSymbol: Option[Symbol] = Symbol.fromString(trimmedAnswer)
    val noAnswerCase: Boolean          = correctAnswer.isEmpty && trimmedAnswer.equals("")

    answerAsSymbol.isDefined &&
    (correctAnswer.contains(answerAsSymbol.get) || noAnswerCase)

  override def isMiniGameFinished: Boolean =
    currentRound == rounds

  @tailrec
  private def generateOperation: String =
    val question       = SyntaxTreeBuilder
      .buildOperationFromComplexity(difficulty.toInt)
      .toString
    val containsAnswer = EvaluateOperation.evaluateOperationFromString(question, List()).nonEmpty

    if (containsAnswer || CAN_GENERATE_WRONG_OPERATIONS) then question else generateOperation

  private def trimQuestion(question: String): String =
    question
      .replaceAll("\\(", "")
      .replaceAll("\\)", "")
      .replace("and ", "\nand\n")
      .replace("or ", "\nor\n")
