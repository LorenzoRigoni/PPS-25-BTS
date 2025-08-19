package models

import utils.constants.FastCalcConstants.*
import scala.util.Random

/**
 * This case class manage the logic of the mini-game "Fast Calc"
 * @param rounds
 *   The total number of rounds
 * @param currentRound
 *   The current round
 * @param difficulty
 *   The current difficulty
 * @param lastQuestion
 *   The last question generated
 */
case class FastCalcLogic(
    rounds: Int,
    currentRound: Int = 0,
    difficulty: Int = 1,
    lastQuestion: Option[SimpleTextQuestion] = None
) extends MiniGameLogic[SimpleTextQuestion, Int, Boolean]
    with MathMiniGameLogic[SimpleTextQuestion]:
  private val NUM_SIMPLE_ROUNDS = 3
  private val NUM_MEDIUM_ROUNDS = 6
  private val MAX_NUM_TERMS     = 4
  private val MAX_NUM           = 10

  private def getRandomNumber(maxNumber: Int): Int =
    Random.nextInt(maxNumber) + 1

  private def getOperatorsForDifficultyLevel(difficulty: Int): Seq[String] =
    difficulty match
      case d if d < NUM_SIMPLE_ROUNDS => Seq("+")
      case d if d < NUM_MEDIUM_ROUNDS => Seq("+", "-")
      case _                          => Seq("+", "-", "*")

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

  /**
   * Method to get a list of tokens (numbers and operators) from an expression string.
   * @param expression
   *   string that contains the expression
   * @return
   *   a list of numbers and operators
   */
  def getListFromExpression(expression: String): List[String] =
    expression.split(" ").toList

  /**
   * Evaluates a mathematical expression represented as a list of tokens.
   * @param expression
   *   expression represented as a list of tokens (numbers and operators)
   * @return
   *   The result of the expression as an Int
   */
  def calculateResult(expression: List[String]): Int =
    /**
     * @param expr
     *   current expression as a list of tokens
     * @param ops
     *   set of operators to search for
     * @param f
     *   a function that combines the results of the left and right sub-expressions
     * @return
     *   Some(result) if an operator from `ops` was found and applied, None otherwise
     */
    def evalOperators(
        expr: List[String],
        ops: Set[String],
        f: (Int, Int, String) => Int
    ): Option[Int] =
      expr.zipWithIndex.findLast { case (token, _) => ops.contains(token) } match
        case Some((operator, index)) =>
          val (left, right) = expr.splitAt(index)
          right match
            case op :: rightPart =>
              val l = calculate(left)
              val r = calculate(rightPart)
              Some(f(l, r, op))
            case _               => throw new IllegalArgumentException("Malformed expression")
        case _                       => None

    /**
     * Evaluates an expression, first checking for + and -, then *
     * @param expr
     *   current expression as a list of tokens
     * @return
     *   an int as a result of the expression
     */
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

  override protected def difficultyStep: Int = FAST_CALC_DIFFICULTY_STEP

  override protected def withNewQuestion(
      question: SimpleTextQuestion
  ): MiniGameLogic[SimpleTextQuestion, Int, Boolean] =
    this.copy(
      currentRound = currentRound + 1,
      difficulty = difficulty + 1 * difficultyStep,
      lastQuestion = Some(question)
    )

  override protected def correctAnswer(question: SimpleTextQuestion): Int =
    calculateResult(getListFromExpression(lastQuestion.get.text))

  override def generateQuestion
      : (MiniGameLogic[SimpleTextQuestion, Int, Boolean], SimpleTextQuestion) =
    val numTerms     = Math.min(difficulty + 1, MAX_NUM_TERMS)
    val operatorsSeq = getOperatorsForDifficultyLevel(difficulty)
    val numbers      = (for _ <- 1 to numTerms yield getRandomNumber(MAX_NUM).toString).toList
    val operators    = (for _ <- 1 until numTerms yield getRandomOperator(operatorsSeq)).toList
    val expression   = buildExpression(numbers, operators).mkString(" ")
    val question     = SimpleTextQuestion(expression)
    advance(question)

  override def isMiniGameFinished: Boolean =
    currentRound == rounds
