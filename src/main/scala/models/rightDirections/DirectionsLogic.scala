package models.rightDirections
import models.MiniGameLogic
import models.rightDirections.structure.SyntaxTreeBuilder
object DirectionsLogic extends MiniGameLogic:
  override def generateQuestion(difficult: Int): String =
    SyntaxTreeBuilder.buildOperationFromComplexity(difficult).toString

  override def validateAnswer[Direction](question: String, answer: Direction): Boolean =
    val a = false
    a
