package models.rightDirections

import models.rightDirections.structure.Token
import models.rightDirections.structure.treeLogic.{BinaryTree, Leaf, Node}
import org.scalatest.funsuite.AnyFunSuite
import models.rightDirections.structure.EvaluateOperation.evaluateOperationFromString as evaluate
import models.rightDirections.structure.Token.Up

class RightDirectionsTests extends AnyFunSuite:
  val operationWithResults    = new Node(Token.Or, new Leaf(Token.Up), Option(new Leaf(Token.Right)))
  val operationWithoutResults =
    new Node(Token.And, new Leaf(Token.Up), Option(new Leaf(Token.Right)))

  val operationWith1Not  = new Node(Token.Not, new Leaf(Token.Up), None)
  val operationWith2Not = new Node(Token.Not, operationWith1Not, None)
  val operationWith3Not = new Node(Token.Not, operationWith2Not, None)

  val operationWith1Result               = new Leaf(Token.Up)
  val operationWith2Results: Node[Token] = operationWithResults
  val operationWith3Results: Node[Token] = operationWith1Not
  val operationWith4Results              = new Node(Token.Or, operationWith1Not, Option(operationWith1Result))

  test("Check if the evaluation of functions works correctly") {
    assert(evaluate(operationWithResults.toString, List()).nonEmpty)
    assert(evaluate(operationWithoutResults.toString, List()).isEmpty)
  }

  test("Check if not gets correctly evaluated") {
    val oneNotResult: Seq[Token] = evaluate(operationWith1Not.toString, List())
    val twoNotResult: Seq[Token] = evaluate(operationWith2Not.toString, List())
    val threeNotResult: Seq[Token] = evaluate(operationWith3Not.toString, List())

    assert(oneNotResult.size == 3)
    assert(twoNotResult.size == 1 && twoNotResult.contains(Up))
    assert(oneNotResult equals threeNotResult)
  }

  test("check if the correct amount of directions are returned") {
    assert(evaluate(operationWith1Result.toString, List()).size == 1)
    assert(evaluate(operationWith2Results.toString, List()).size == 2)
    assert(evaluate(operationWith3Results.toString, List()).size == 3)
    assert(evaluate(operationWith4Results.toString, List()).size == 4)
  }
