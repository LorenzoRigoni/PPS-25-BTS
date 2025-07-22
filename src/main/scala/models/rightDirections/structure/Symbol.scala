package models.rightDirections.structure

enum Symbol(val complexity: Int):
  private case And extends Symbol(2)
  private case Or extends Symbol(2)
  private case Not extends Symbol(1)
  private case X extends Symbol(0)
  private case LP extends Symbol(0)
  private case RP extends Symbol(0)
  private case Up extends Symbol(0)
  private case Down extends Symbol(0)
  private case Left extends Symbol(0)
  private case Right extends Symbol(0)

  override def toString: String = this match
    case And => "and"
    case Or => "or"
    case Not => "not"
    case X => "x"
    case LP => "("
    case RP => ")"
    case Up => "up"
    case Down => "down"
    case Left => "left"
    case Right => "right"

object Symbol:
  def allOperators: List[Symbol] = List(And, Or, Not)
  def allDirections: List[Symbol] = List(Up, Left, Right, Down)
  def all: List[Symbol] = List(And, Or, Not,Up, Left, Right, Down, X,LP,RP)

  def fromString(s: String): Option[Symbol] = s.toLowerCase match
    case "and" => Some(And)
    case "or" => Some(Or)
    case "not"  => Some(Not)
    case "x"  => Some(X)
    case "up" => Some(Up)
    case "down" => Some(Down)
    case "left"  => Some(Left)
    case "right"  => Some(Right)
    case _ => None

  def getMaximumComplexity:Int=
    all.map(_.complexity).max

  def getAnyOperatorBelowCertainComplexity(complexity:Int) : Symbol=
    val candidates = allOperators.concat(allDirections).filter(_.complexity <= complexity)
    val max = candidates.map(_.complexity).maxOption.getOrElse(-1)
    scala.util.Random.shuffle(candidates.filter(_.complexity == max)).head
