package models.rightDirections.structure

enum Symbol(val complexity: Int):
  private case And extends Symbol(2)
  private case Or extends Symbol(2)
  private case Not extends Symbol(1)
  private case X extends Symbol(Integer.MAX_VALUE)
  private case Up extends Symbol(0)
  private case Down extends Symbol(0)
  private case Left extends Symbol(0)
  private case Right extends Symbol(0)

object Symbol:
  def allOperators: List[Symbol] = List(And, Or, Not)
  def allDirections: List[Symbol] = List(Up, Left, Right, Down)

  def fromString(s: String): Option[Symbol] = s.toLowerCase match
    case "and" => Some(And)
    case "or" => Some(Or)
    case "not"  => Some(Not)
    case "x"  => Some(X)
    case "up" => Some(Up)
    case "down" => Some(Down)
    case "left"  => Some(Left)
    case "right"  => Some(Right)
    
  def getMaximumComplexity:Int=
    allOperators.concat(allDirections).map(_.complexity).max

  def getAnyOperatorBelowCertainComplexity(complexity:Int) : Option[Symbol] =
    val candidates = allOperators.concat(allDirections).filter(_.complexity < complexity)
    scala.util.Random.shuffle(candidates).headOption