package models.rightDirections
import models.rightDirections.structure.Symbol
import models.MiniGameLogic
import models.rightDirections.structure.{EvaluateOperation, SyntaxTreeBuilder}
object RightDirectionsLogic extends MiniGameLogic:
  override def generateQuestion(difficult: Int): String =
    SyntaxTreeBuilder.buildOperationFromComplexity(difficult).toString

  override def validateAnswer[A](question: String, answer: A): Boolean = answer match {
    case s: String =>
      val normalizedAnswer = s.toLowerCase.trim
      val correctAnswer    = EvaluateOperation.evaluateOperationFromString(question, List())
      correctAnswer.contains(
        Symbol.fromString(normalizedAnswer).get
      ) || (correctAnswer.isEmpty && normalizedAnswer.equals(""))
  }
