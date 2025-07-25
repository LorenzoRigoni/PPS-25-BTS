package models.rightDirections.structure

enum Symbol(val complexity: Int):
  case And   extends Symbol(2)
  case Or    extends Symbol(2)
  case Not   extends Symbol(1)
  case X     extends Symbol(0)
  case LP    extends Symbol(0)
  case RP    extends Symbol(0)
  case Up    extends Symbol(0)
  case Down  extends Symbol(0)
  case Left  extends Symbol(0)
  case Right extends Symbol(0)

  override def toString: String = this match
    case And   => "and"
    case Or    => "or"
    case Not   => "not"
    case X     => "x"
    case LP    => "("
    case RP    => ")"
    case Up    => "up"
    case Down  => "down"
    case Left  => "left"
    case Right => "right"

object Symbol:
  def allOperators: List[Symbol]  = List(And, Or, Not)
  def allDirections: List[Symbol] = List(Up, Left, Right, Down)
  def all: List[Symbol]           = List(And, Or, Not, Up, Left, Right, Down, X, LP, RP)

  def getMaximumComplexity: Int =
    all.map(_.complexity).max

  def getAnyOperatorBelowCertainComplexity(complexity:Int) : Symbol=
    allOperators.concat(allDirections).filter(op => op.complexity <= complexity).sortBy(_ => Math.random()).maxBy(_.complexity)

  def fromString(str: String): Option[Symbol] = str.toLowerCase match
    case "and"   => Some(And)
    case "or"    => Some(Or)
    case "not"   => Some(Not)
    case "x"     => Some(X)
    case "("     => Some(LP)
    case ")"     => Some(RP)
    case "up"    => Some(Up)
    case "down"  => Some(Down)
    case "left"  => Some(Left)
    case "right" => Some(Right)
    case _       => None
