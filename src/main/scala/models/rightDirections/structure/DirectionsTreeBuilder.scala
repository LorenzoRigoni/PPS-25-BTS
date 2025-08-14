package models.rightDirections.structure

import models.rightDirections.structure.Token.X
import models.rightDirections.structure.treeLogic.{BinaryTree, Leaf}
import scala.util.Random
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
          case (0, c) if c >= 2     => 2
          case (c1, c2) if c1 != c2 => 1
          case _                    => 0

        val tokenToAdd = Token.randomOperatorUpTo(TokensNextComplexity)
        given Random   = new Random()
        expandTree(
          tokenToAdd.complexity match
            case 2 => root.expand(X, tokenToAdd, Some(X), Some(X))
            case 1 => root.expand(X, tokenToAdd, Some(X), None)
            case 0 => root.expand(X, tokenToAdd, None, None)
            case _ => root
          ,
          complexity
        )

  override def calculateTreeComplexity(root: BinaryTree[Token]): Int =
    val leftComplexity  = root.left.map(calculateTreeComplexity).getOrElse(0)
    val rightComplexity = root.right.map(calculateTreeComplexity).getOrElse(0)
    root.value.complexity + leftComplexity + rightComplexity
