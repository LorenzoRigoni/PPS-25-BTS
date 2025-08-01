package models

import utils.FastCalcConstants.*
import scala.util.Random

case class FastCalcLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int,
    lastQuestion: Option[String] = None
) extends MiniGameLogic:

  private def hasNextRound: Boolean =
    currentRound < rounds

  private def getOperatorsForDifficultyLevel(difficulty: Int): Seq[String] = difficulty match
    case 1          => Seq("+")
    case d if d < 4 => Seq("+", "-")
    case _          => Seq("+", "-", "*")

  private def getRandomNumber(maxNumber: Int): Int =
    Random.nextInt(maxNumber) + 1

  private def getRandomOperator(operators: Seq[String]): String =
    val operatorIndex = Random.nextInt(operators.length)
    operators(operatorIndex)

  private def buildExpression(
      numbers: List[String],
      operators: List[String]
  ): List[String] = (numbers, operators) match
    case (n1 :: Nil, Nil)                                => List(n1)
    case (n1 :: n2 :: nextNumbers, op1 :: nextOperators) =>
      n1 :: op1 :: buildExpression(n2 :: nextNumbers, nextOperators)
    case _                                               => Nil

  def getListFromExpression(expression: String): Seq[String] =
    expression.split(" ").toList

  def calculateResult(expression: Seq[String]): Int =
    def calculate(expression: Seq[String]): Int = expression match
      case n :: Nil => n.toInt
      case _        =>
        val index = expression.lastIndexWhere(e => e == "+" || e == "-")
        if index != -1 then
          val (left, operator :: right) = expression.splitAt(index)
          val l                         = calculate(left)
          val r                         = calculate(right)
          operator match
            case "+" => l + r
            case "-" => l - r
        else
          val index = expression.lastIndexWhere(e => e == "*" || e == "/")
          if index != -1 then
            val (left, operator :: right) = expression.splitAt(index)
            val l                         = calculate(left)
            val r                         = calculate(right)
            operator match
              case "*" => l * r
          else throw new IllegalArgumentException(s"Malformed expression!")
    calculate(expression)

  override def generateQuestion: (MiniGameLogic, String) =
    val numTerms     = Math.min(difficulty + 1, MAX_NUM_TERMS)
    val maxNumber    = MAX_NUMBER
    val operatorsSeq = getOperatorsForDifficultyLevel(difficulty)

    val numbers    = (1 to numTerms).map(_ => getRandomNumber(maxNumber).toString).toList
    val operators  = (1 until numTerms).map(_ => getRandomOperator(operatorsSeq)).toList
    val expression = buildExpression(numbers, operators).mkString(" ")
    (
      this.copy(
        currentRound = currentRound + 1,
        difficulty = difficulty + FAST_CALC_DIFFICULTY_STEP,
        lastQuestion = Some(expression)
      ),
      expression
    )

  override def validateAnswer[Int](answer: Int): Boolean =
    calculateResult(getListFromExpression(lastQuestion.get)) == answer

  override def isMiniGameFinished: Boolean =
    !hasNextRound
