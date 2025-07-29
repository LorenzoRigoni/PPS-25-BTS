package models.rightDirections.structure

import models.rightDirections.structure.Symbol.X
import models.rightDirections.structure.treeLogic.{BinaryTree, Leaf}

import scala.annotation.tailrec

object SyntaxTreeBuilder extends OperationBuilder[Symbol] {

  override def buildOperationFromString(input: String, formatter: String => String): Option[BinaryTree[Symbol]] =
    None

  override def buildOperationFromComplexity(complexity: Int): BinaryTree[Symbol] =
    expandTree(new Leaf(X), complexity)

  override protected def formatString(input: String): String = {
    val explodedString = input.split(" ")
    val possibleOperators = Symbol.all.map(_.toString)
    val cleanedArray = explodedString.map(_.toLowerCase).intersect(possibleOperators)
    cleanedArray.mkString(" ")
  }

  @tailrec
  private def expandTree(tree: BinaryTree[Symbol], complexity: Int): BinaryTree[Symbol] = {
    if(!tree.contains(X)) tree
    else{
      val treeComplexity = calculateTreeComplexity(tree)
      if (treeComplexity > complexity) tree
      else {
          val symbolsNextComplexity = (treeComplexity, complexity) match {
            case (0, c) if c >= 2 => 2
            case (c1,c2) if c1 !=c2 => 1
            case _ => 0
          }
          val symbolToAdd = Symbol.getAnyOperatorBelowCertainComplexity(symbolsNextComplexity)
          symbolToAdd.complexity match {
            case 2 => expandTree(tree.expand(X, symbolToAdd, Option(X), Option(X)),complexity)
            case 1 => expandTree(tree.expand(X, symbolToAdd,Option(X),None),complexity)
            case 0 => expandTree(tree.expand(X, symbolToAdd, None, None),complexity)
            case _ => tree
          }
        }
      }
    }



  private def calculateTreeComplexity(base: BinaryTree[Symbol]): Int = {
    val leftComplexity = base.left.map(calculateTreeComplexity).getOrElse(0)
    val rightComplexity = base.right.map(calculateTreeComplexity).getOrElse(0)
    base.value.complexity + leftComplexity + rightComplexity
  }
}