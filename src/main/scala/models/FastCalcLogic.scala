package models

object FastCalcLogic extends MiniGameLogic:
  private val rand = new scala.util.Random()

  private def getOperatorsForDifficultyLevel(difficulty: Int): List[String] = difficulty match
    case 1 => List("+")
    case d if d < 3 => List("+", "-")
    case d if d < 6 => List("+", "-", "*")
    case _ => List("+", "-", "*", "/")

  private def getRandomNumber(maxNumber: Int): Int =
    rand.between(1, maxNumber + 1)

  private def getRandomOperator(operatorsList: List[String]): String =
    operatorsList(rand.nextInt(operatorsList.length))

  private def buildExpression(numbersList: List[String], operatorsList: List[String]): List[String] = (numbersList, operatorsList) match
    case (n1::Nil, Nil) => List(n1)
    case (n1::n2::nextNumbers, op1::nextOperators) => n1::op1::buildExpression(n2::nextNumbers, nextOperators)
    case _ => Nil

  def getListFromExpression(expression: String): List[String] =
    expression.split(" ").toList

  def calculateResult(expressionList: List[String]): Int =
    def calculate(expressionList: List[String]) : Int = expressionList match
      case n::Nil => n.toInt
      case _ =>
        val index = expressionList.lastIndexWhere(e => e == "+" || e == "-")
        if index != -1 then
          val (left, operator::right) = expressionList.splitAt(index)
          val l = calculate(left)
          val r = calculate(right)
          operator match
            case "+" => l + r
            case "-" => l - r
        else
          val index = expressionList.lastIndexWhere(e => e == "*" || e == "/")
          if index != -1 then
            val (left, operator :: right) = expressionList.splitAt(index)
            val l = calculate(left)
            val r = calculate(right)
            operator match
              case "*" => l * r
              case "/" => l / r
          else
            throw new IllegalArgumentException(s"Malformed expression!")
    calculate(expressionList)

  //TODO: ridurre difficoltà, i livelli alti sono troppo difficili da fare a mente
  override def generateQuestion(difficultyLevel: Int): String =
    val numTerms = Math.min(1 + difficultyLevel, 6) //per ora massimo 6 termini
    val maxNumber = 10 * difficultyLevel //nell'ipotesi che ci siano 10 livelli
    val operatorsList = getOperatorsForDifficultyLevel(difficultyLevel)
    val numbers = List.fill(numTerms)(getRandomNumber(maxNumber).toString)
    val operators = List.fill(numTerms - 1)(getRandomOperator(operatorsList))
    val expression = buildExpression(numbers, operators)
    expression.mkString(" ") //method used to add a separator between list elements


  override def validateAnswer[Int](question: String, answer: Int): Boolean =
    calculateResult(getListFromExpression(question)) == answer