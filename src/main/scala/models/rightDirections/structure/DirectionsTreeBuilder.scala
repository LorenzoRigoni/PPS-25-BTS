package models.rightDirections.structure

import models.rightDirections.structure.Token.X
import models.rightDirections.structure.treeLogic.*

import scala.util.Random
import utils.constants.RightDirectionsConstants.*

import scala.annotation.tailrec

object DirectionsTreeBuilder extends OperationBuilder[Token]:

  override def buildOperationFromComplexity(complexity: Int): BinaryTree[Token] =
    expandTree(new Leaf(X), complexity)

  @tailrec
  override def expandTree(root: BinaryTree[Token], complexity: Int): BinaryTree[Token] =
    if (!root.contains(X)) root
    else
      val treeComplexity = calculateTreeComplexity(root)
      if (treeComplexity > complexity) root
      else
        val TokensNextComplexity = (treeComplexity, complexity) match
          case (0, c) if c >= BINARY_OPERATOR_COMPLEXITY => BINARY_OPERATOR_COMPLEXITY
          case (c1, c2) if c1 != c2                      => UNARY_OPERATOR_COMPLEXITY
          case _                                         => SUBSTITUTE_OPERATOR_COMPLEXITY
        val tokenToAdd           = Token.randomOperatorUpTo(TokensNextComplexity)
        expandTree(
          tokenToAdd.complexity match
            case BINARY_OPERATOR_COMPLEXITY     => root.expand(X, tokenToAdd, Some(X), Some(X))
            case UNARY_OPERATOR_COMPLEXITY      => root.expand(X, tokenToAdd, Some(X), None)
            case SUBSTITUTE_OPERATOR_COMPLEXITY => root.expand(X, tokenToAdd, None, None)
            case _                              => root
          ,
          complexity
        )

  override def calculateTreeComplexity(root: BinaryTree[Token]): Int =
    val leftComplexity  = root.left.map(calculateTreeComplexity).getOrElse(0)
    val rightComplexity = root.right.map(calculateTreeComplexity).getOrElse(0)
    root.value.complexity + leftComplexity + rightComplexity
