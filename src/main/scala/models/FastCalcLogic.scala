package models

import utils.FastCalcConstants.*
import scala.util.Random
import utils.SimpleTextQuestion

/**
 * This case class manages the logic of the Fast Calc mini-game.
 */
case class FastCalcLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[SimpleTextQuestion] = None
) extends MiniGameLogic[SimpleTextQuestion, Int, Boolean]:

  private def getRandomNumber(maxNumber: Int): Int =
    Random.nextInt(maxNumber) + 1

  private def getOperatorsForDifficultyLevel(difficulty: Int): Seq[String] =
    val numSimpleRounds = 3
    val numMediumRounds = 6
    difficulty match
      case d if d < numSimpleRounds => Seq("+")
      case d if d < numMediumRounds => Seq("+", "-")
      case _                        => Seq("+", "-", "*")

  private def getRandomOperator(operators: Seq[String]): String =
    val operatorIndex = Random.nextInt(operators.length)
    operators(operatorIndex)

  private def buildExpression(
      numbers: List[String],
      operators: List[String]
  ): List[String] =
    for
      (n, op) <- numbers.zipAll(operators, "", "")
      token   <- List(n, op) if token.nonEmpty
    yield token

  def getListFromExpression(expression: String): List[String] =
    expression.split(" ").toList

  def calculateResult(expression: List[String]): Int =
    def evalOperators(
        expr: List[String],
        ops: Set[String],
        f: (Int, Int, String) => Int
    ): Option[Int] =
      val index = expr.lastIndexWhere(ops.contains)
      if index == -1 then None
      else
        val (left, right) = expr.splitAt(index)
        right match
          case op :: rightPart =>
            val l = calculate(left)
            val r = calculate(rightPart)
            Some(f(l, r, op))
          case _               => throw new IllegalArgumentException("Malformed expression")

    def calculate(expr: List[String]): Int = expr match
      case n :: Nil => n.toInt
      case _        =>
        evalOperators(expr, Set("+", "-"), (l, r, op) => if op == "+" then l + r else l - r) match
          case Some(value) => value
          case None        =>
            evalOperators(
              expr,
              Set("*"),
              (l, r, op) => l * r
            ) match
              case Some(value) => value
              case None        => throw new IllegalArgumentException("Malformed expression")
    calculate(expression)

  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
    val maxNumTerms  = 4
    val maxNumber    = 10
    val numTerms     = Math.min(difficulty + 1, maxNumTerms)
    val operatorsSeq = getOperatorsForDifficultyLevel(difficulty)
    val numbers      = (1 to numTerms).map(_ => getRandomNumber(maxNumber).toString).toList
    val operators    = (1 until numTerms).map(_ => getRandomOperator(operatorsSeq)).toList
    val expression   = buildExpression(numbers, operators).mkString(" ")
    val question     = SimpleTextQuestion(expression)
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + 1 * FAST_CALC_DIFFICULTY_STEP,
        lastQuestion = Some(question)
      ),
      question
    )

  override def parseAnswer(answer: String): Int =
    answer.trim.toIntOption.getOrElse(
      throw IllegalArgumentException(s"$answer is not an Int")
    )

  override def validateAnswer(answer: Int): Boolean =
    calculateResult(getListFromExpression(lastQuestion.get.text)) == answer

  override def isMiniGameFinished: Boolean =
    !(currentRound < rounds)
