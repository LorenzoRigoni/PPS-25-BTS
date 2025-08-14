package models.rightDirections.structure

object EvaluateOperation:
  private def stripParentheses(s: String): String =
    s.replace("(", "").replace(")", "").trim

  def evaluateOperationFromString(input: String, currentList: Seq[Token]): Seq[Token] =
    val trimmedInput = stripParentheses(input)

    trimmedInput match
      case operation if operation.contains("and") =>
        val Array(left, right) = operation.split("and")
        combineAnd(left, right)

      case operation if operation.contains("or") =>
        val Array(left, right) = operation.split("or")
        combineOr(left, right)

      case operation if !(operation.contains("x") || operation.contains("not")) =>
        Seq(Token.fromString(stripParentheses(operation)))

      case operation =>
        handleNotCondition(operation)

  private def combineAnd(left: String, right: String): Seq[Token] =
    (evaluateOperationFromString(left, Nil) intersect
      evaluateOperationFromString(right, Nil)).distinct

  private def combineOr(left: String, right: String): Seq[Token] =
    (evaluateOperationFromString(left, Nil) concat
      evaluateOperationFromString(right, Nil)).distinct

  private def handleNotCondition(trimmed: String): Seq[Token] =
    val operator          = Token fromString
      trimmed
        .split(" ")
        .lastOption
        .getOrElse("")
    val numberOfNegations = trimmed.split(" ").count(_ == "not")

    if numberOfNegations % 2 == 0 then Seq(operator)
    else Token.directions.filterNot(_ == operator)
