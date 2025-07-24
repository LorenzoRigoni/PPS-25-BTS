package models.rightDirections.structure

import models.rightDirections.structure.Symbol.{And, Or}
import models.rightDirections.structure.treeLogic.BinaryTree

import java.util

object EvaluateOperation:
  def evaluateOperationFromTree(tree: BinaryTree[Symbol]): List[Symbol] = {
    val leftResult  = tree.left.map(evaluateOperationFromTree)
    val rightResult = tree.right.map(evaluateOperationFromTree)

    tree.value match {
      case And   => leftResult.get.intersect(rightResult.get)
      case Or    => leftResult.get.concat(rightResult.get)
      case value =>
        leftResult match {
          case None => List(value)
          case _    =>
            Symbol.allDirections.filterNot(_ == value)
        }
    }
  }
