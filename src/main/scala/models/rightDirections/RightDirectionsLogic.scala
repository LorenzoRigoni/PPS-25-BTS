package models.rightDirections

import models.rightDirections.structure.Symbol
import models.MiniGameLogic
import models.rightDirections.structure.{EvaluateOperation, SyntaxTreeBuilder}

case class RightDirectionsLogic(rounds: Int, difficulty: Int) extends MiniGameLogic:
  override def generateQuestion: (MiniGameLogic, String) =
    (
      this.copy(), // TODO: increase difficulty here
      SyntaxTreeBuilder
        .buildOperationFromComplexity(1 /*TODO: gestire difficoltà con case class*/ )
        .toString
    )

  override def validateAnswer[A](question: String, answer: A): Boolean = answer match {
    case s: String =>
      val normalizedAnswer = s.toLowerCase.trim
      val correctAnswer    = EvaluateOperation.evaluateOperationFromString(question, List())
      correctAnswer.contains(
        Symbol.fromString(normalizedAnswer).get
      ) || (correctAnswer.isEmpty && normalizedAnswer.equals(""))
  }

  override def isMiniGameFinished: Boolean = ???
