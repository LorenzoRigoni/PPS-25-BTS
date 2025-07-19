package models.rightDirections

enum Operator(val complexity: Int):
  private case And extends Operator(2)
  private case Or extends Operator(2)
  private case Not extends Operator(1)
  case X extends Operator(0)

object Operator:
  def all: List[Operator] = List(And, Or, Not, X)

  def fromString(s: String): Option[Operator] = s.toLowerCase match
    case "and" => Some(And)
    case "or" => Some(Or)
    case "not"  => Some(Not)
    case "x"  => Some(X)
    
  def getMaximumComplexity:Int=
    all.map(_.complexity).max