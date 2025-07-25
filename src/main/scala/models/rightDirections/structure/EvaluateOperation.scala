package models.rightDirections.structure

import models.rightDirections.structure.Symbol.{And, Not, Or, RP, Up}
import models.rightDirections.structure.treeLogic.BinaryTree

import java.util
import scala.annotation.tailrec

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

  def evaluateOperationFromString(input: String, currentList: List[Symbol]): List[Symbol] = {
    if(input.contains("and"))
      return evaluateOperationFromString(input.split("and")(0),List()).intersect(evaluateOperationFromString(input.split("and")(1),List())).distinct
    if (input.contains("or"))
      return evaluateOperationFromString(input.split("or")(0),List()).concat(evaluateOperationFromString(input.split("or")(1),List())).distinct

    if(!(input.contains("x") || input.contains("not")))
      return List(Symbol.fromString(input.replace("(","").replace(")","").trim()).get)

    val partialExtraction = input.split('(').find(_.contains(")"))
    if (partialExtraction.isEmpty || !input.contains('('))
      return currentList

    val nextOperation = partialExtraction.get.split(')')(0)
    val newString = input.replace("("+nextOperation+")".trim(),"x")

    val newList: List[Symbol] =
      if (nextOperation.contains("x")) currentList
      else List(Symbol.fromString(nextOperation.split(" ")(0)).get)

    evaluateOperationFromString(newString,Symbol.allDirections.filterNot(newList.contains(_)))
  }