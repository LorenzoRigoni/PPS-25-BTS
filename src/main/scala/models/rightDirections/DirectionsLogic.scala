package models.rightDirections
import models.MiniGameLogic
import models.rightDirections.structure.{EvaluateOperation, SyntaxTreeBuilder}
object DirectionsLogic extends MiniGameLogic:
  override def generateQuestion(difficult: Int): String =
    SyntaxTreeBuilder.buildOperationFromComplexity(difficult).toString

  override def validateAnswer[Direction](question: String, answer: Direction): Boolean =
    EvaluateOperation.evaluateOperationFromString(question,List()).contains(answer)