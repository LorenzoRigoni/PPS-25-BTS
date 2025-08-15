package models.rightDirections.structure

import scala.util.Random

enum Token(val complexity: Int):
  case And   extends Token(2)
  case Or    extends Token(2)
  case Not   extends Token(1)
  case X     extends Token(0)
  case LP    extends Token(0)
  case RP    extends Token(0)
  case Up    extends Token(0)
  case Down  extends Token(0)
  case Left  extends Token(0)
  case Right extends Token(0)
  case Empty extends Token(0)

  override def toString: String = this match
    case Token.And   => "and"
    case Token.Or    => "or"
    case Token.Not   => "not"
    case Token.X     => "x"
    case Token.LP    => "("
    case Token.RP    => ")"
    case Token.Up    => "up"
    case Token.Down  => "down"
    case Token.Left  => "left"
    case Token.Right => "right"
    case Token.Empty => ""

object Token:
  val operators: Seq[Token]  = Seq(And, Or, Not)
  val directions: Seq[Token] = Seq(Up, Left, Right, Down)
  val others: Seq[Token]     = Seq(X, Empty, LP, RP)
  val all: Seq[Token]        = operators concat directions concat others

  def maximumComplexity: Int =
    all.iterator.map(_.complexity).max

  def mostComplexUpTo(maxComplexity: Int): Option[Token] =
    val filtered = all.filter(_.complexity <= maxComplexity)
    if (filtered.isEmpty) None
    else
      val maxVal = filtered.map(_.complexity).max
      val tied   = filtered.filter(_.complexity == maxVal)
      Some(tied(Random.nextInt(tied.length)))

  def randomOperatorUpTo(maxComplexity: Int)(using rng: Random): Token =
    val complexityToUse =
      if maxComplexity >= 2 then rng.nextInt(3) + 1
      else maxComplexity

    val filtered = rng
      .shuffle(operators concat directions)
      .filter(_.complexity <= complexityToUse)
    filtered.maxBy(_.complexity)

  def fromString(str: String): Token =
    all.find(_.toString.equals(str.trim)).getOrElse(Empty)
