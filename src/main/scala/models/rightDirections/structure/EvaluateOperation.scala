package models.rightDirections.structure
import scala.List

object EvaluateOperation:
  private def stripParentheses(s: String): String =
    s.replace("(", "").replace(")", "").trim

  def evaluateOperationFromString(input: String, currentList: List[Symbol]): List[Symbol]  = {
    val trimmed: String = input.trim

    if (trimmed.contains("and")) {
      val Array(left, right) = trimmed.split("and", 2)
      evaluateOperationFromString(left, Nil)
        .intersect(evaluateOperationFromString(right, Nil))
        .distinct
    } else if (trimmed.contains("or")) {
      val Array(left, right) = trimmed.split("or", 2)
      evaluateOperationFromString(left, Nil)
        .concat(evaluateOperationFromString(right, Nil))
        .distinct
    } else if (!(trimmed.contains("x") || trimmed.contains("not")))
      List(Symbol.fromString(stripParentheses(trimmed)).get)
    else
      handleNotCondition(trimmed, currentList)
  }
  private def handleNotCondition(trimmed: String, currentList: List[Symbol]): List[Symbol] =
    val partialExtractionOpt = trimmed.split('(').find(_.contains(')'))

    partialExtractionOpt match {
      case Some(nextOperationRaw) if trimmed.contains('(') =>
        val nextOperation = nextOperationRaw.split(')')(0)
        val replaced      = trimmed.replace(s"($nextOperation)", "x").trim

        val newList: List[Symbol] =
          if (nextOperation.contains("x")) currentList
          else List(Symbol.fromString(nextOperation.split(" ")(1)).get)

        val filtered = Symbol.allDirections.filterNot(newList.contains)
        evaluateOperationFromString(replaced, filtered)

      case _ =>
        if currentList.nonEmpty then currentList
        else List(Symbol.fromString("").get)
    }
