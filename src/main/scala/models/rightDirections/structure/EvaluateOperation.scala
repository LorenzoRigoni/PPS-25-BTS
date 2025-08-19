package models.rightDirections.structure

import models.rightDirections.structure.Symbol.{And, Not, Or, RP, Up}
import models.rightDirections.structure.treeLogic.BinaryTree

import java.util
import scala.annotation.tailrec

object EvaluateOperation:
  private def evaluateOperationFromTree(tree: BinaryTree[Symbol]): List[Symbol] = {
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
    val trimmed = input.trim

    def stripParentheses(s: String): String =
      s.replace("(", "").replace(")", "").trim

    trimmed match {
      case s if s.contains("and") =>
        val Array(left, right) = s.split("and", 2)
        evaluateOperationFromString(left, Nil)
          .intersect(evaluateOperationFromString(right, Nil))
          .distinct

      case s if s.contains("or") =>
        val Array(left, right) = s.split("or", 2)
        evaluateOperationFromString(left, Nil)
          .concat(evaluateOperationFromString(right, Nil))
          .distinct

      case s if !(s.contains("x") || s.contains("not")) =>
        List(Symbol.fromString(stripParentheses(s)).get)

      case s =>
        val partialExtractionOpt = s.split('(').find(_.contains(')'))

        partialExtractionOpt match {
          case Some(nextOperationRaw) if s.contains('(') =>
            val nextOperation = nextOperationRaw.split(')')(0)
            val replaced      = s.replace(s"($nextOperation)", "x").trim

            val newList: List[Symbol] =
              if (nextOperation.contains("x")) currentList
              else List(Symbol.fromString(nextOperation.split(" ")(1)).get)

            val filtered = Symbol.allDirections.filterNot(newList.contains)
            evaluateOperationFromString(replaced, filtered)

          case _ =>
            if currentList.nonEmpty then currentList
            else List(Symbol.fromString("").get)
        }
    }
  }
