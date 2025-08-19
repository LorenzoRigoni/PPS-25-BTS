package models.rightDirections.structure

import scala.util.Random

/**
 * Represents different kinds of tokens with an associated complexity value.
 *
 * @param complexity
 *   integer representing the complexity of the token
 */
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

  /**
   * Provides the string representation of the token.
   *
   * @return
   *   the corresponding string for each token
   */
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

  /**
   * Returns the maximum complexity among all tokens.
   *
   * @return
   *   highest complexity value
   */
  def maximumComplexity: Int =
    all.iterator.map(_.complexity).max

  /**
   * Selects a random operator or direction token with complexity not exceeding `maxComplexity`.
   *
   * @param maxComplexity
   *   maximum allowed complexity
   * @return
   *   a randomly chosen token fitting the complexity constraint
   */
  def randomOperatorUpTo(maxComplexity: Int): Token =
    val complexityToUse =
      if maxComplexity >= 2 then Random.nextInt(3) + 1
      else maxComplexity
    val filtered        = Random
      .shuffle(operators concat directions)
      .filter(_.complexity <= complexityToUse)
    filtered.maxBy(_.complexity)

  /**
   * Parses a string and returns the corresponding token.
   *
   * @param str
   *   input string
   * @return
   *   matching Token, or Empty if not found
   */
  def fromString(str: String): Token =
    all.find(_.toString.equals(str.trim)).getOrElse(Empty)
