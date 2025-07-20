package models.rightDirections.structure

enum Symbols(val complexity: Int):
  private case And extends Symbols(2)
  private case Or extends Symbols(2)
  private case Not extends Symbols(1)
  private case X extends Symbols(Integer.MAX_VALUE)
  private case Up extends Symbols(0)
  private case Down extends Symbols(0)
  private case Left extends Symbols(0)
  private case Right extends Symbols(0)

object Symbols:
  def allOperators: List[Symbols] = List(And, Or, Not)
  def allDirections: List[Symbols] = List(Up, Left, Right, Down)

  def fromString(s: String): Option[Symbols] = s.toLowerCase match
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

  def getAnyOperatorBelowCertainComplexity(complexity:Int) : Option[Symbols] =
    val candidates = allOperators.concat(allDirections).filter(_.complexity < complexity)
    scala.util.Random.shuffle(candidates).headOption