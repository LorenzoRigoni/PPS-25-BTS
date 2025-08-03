package models.rightDirections

import models.rightDirections.structure.Symbol
import models.MiniGameLogic
import models.rightDirections.structure.{EvaluateOperation, SyntaxTreeBuilder}
import utils.RightDirectionsConstants.*

case class RightDirectionsLogic(
    rounds: Int,
    difficulty: Float = 0,
    lastQuestion: Option[String] = None,
    currentRound: Int = 0
) extends MiniGameLogic[String, Boolean]:

  override def generateQuestion: (MiniGameLogic[String, Boolean], String) =
    val question =
      SyntaxTreeBuilder
        .buildOperationFromComplexity(difficulty.toInt)
        .toString
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
    val correctAnswer                  = EvaluateOperation.evaluateOperationFromString(lastQuestion.get, List())
    val answerAsSymbol: Option[Symbol] = Symbol.fromString(trimmedAnswer)
    val noAnswerCase: Boolean          = correctAnswer.isEmpty && trimmedAnswer.equals("")

    answerAsSymbol.isDefined &&
    (correctAnswer.contains(answerAsSymbol.get) || noAnswerCase)

  override def isMiniGameFinished: Boolean =
    rounds == MAX_NUMBER_OF_ROUNDS
