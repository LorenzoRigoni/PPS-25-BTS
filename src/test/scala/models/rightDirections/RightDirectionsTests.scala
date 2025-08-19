package models.rightDirections

import models.rightDirections.structure.Token
import models.rightDirections.structure.treeLogic.{BinaryTree, Leaf, Node}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import models.rightDirections.structure.EvaluateOperation.evaluateOperationFromString as evaluate
import models.rightDirections.structure.Token.Up
import org.scalatest.matchers.must.Matchers.not

class RightDirectionsTests extends AnyFunSuite with Matchers:
  val operationWithResults    = new Node(Token.Or, new Leaf(Token.Up), Option(new Leaf(Token.Right)))
  val operationWithoutResults =
    new Node(Token.And, new Leaf(Token.Up), Option(new Leaf(Token.Right)))

  val operationWith1Not = new Node(Token.Not, new Leaf(Token.Up), None)
  val operationWith2Not = new Node(Token.Not, operationWith1Not, None)
  val operationWith3Not = new Node(Token.Not, operationWith2Not, None)
  val ONE_NOT_SIZE      = 3
  val TWO_NOT_RESULT    = 1

  val operationWith1Result               = new Leaf(Token.Up)
  val operationWith2Results: Node[Token] = operationWithResults
  val operationWith3Results: Node[Token] = operationWith1Not
  val operationWith4Results              = new Node(Token.Or, operationWith1Not, Option(operationWith1Result))
  val ONE_RESULT                         = 1
  val TWO_RESULTS                        = 2
  val THREE_RESULTS                      = 3
  val FOUR_RESULTS                       = 4

  test("Check if the evaluation of functions works correctly") {
    evaluate(operationWithResults.toString, List()) should not be empty
    evaluate(operationWithoutResults.toString, List()) shouldBe empty
  }

  test("Check if not gets correctly evaluated") {
    val oneNotResult: Seq[Token]   = evaluate(operationWith1Not.toString, List())
    val twoNotResult: Seq[Token]   = evaluate(operationWith2Not.toString, List())
    val threeNotResult: Seq[Token] = evaluate(operationWith3Not.toString, List())

    oneNotResult.size shouldBe ONE_NOT_SIZE
    twoNotResult.size shouldBe TWO_NOT_RESULT
    twoNotResult.contains(Up) shouldBe true
    oneNotResult equals threeNotResult shouldBe true
  }

  test("check if the correct amount of directions are returned") {
    evaluate(operationWith1Result.toString, List()).size shouldBe ONE_RESULT
    evaluate(operationWith2Results.toString, List()).size shouldBe TWO_RESULTS
    evaluate(operationWith3Results.toString, List()).size shouldBe THREE_RESULTS
    evaluate(operationWith4Results.toString, List()).size shouldBe FOUR_RESULTS
  }
