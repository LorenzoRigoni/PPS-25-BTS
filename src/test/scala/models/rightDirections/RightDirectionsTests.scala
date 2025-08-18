package models.rightDirections

//import models.rightDirections.structure.Symbol.{And, Not, Or, X}
import models.rightDirections.structure.treeLogic.{BinaryTree, Leaf, Node}
//import models.rightDirections.structure.{Symbol, *}
import org.scalatest.funsuite.AnyFunSuite

class RightDirectionsTests extends AnyFunSuite:
  //val tree: BinaryTree[Symbol] = new Leaf(X).expand(X, Symbol.Left, Option(Not), None)

  test("Check Not operator") {
    // EvaluateOperation.evaluateOperationFromString("(((((right or right) not) or (left and up)) not) not)",List())
    // SyntaxTreeBuilder.expandTree(new Node(Symbol.Not,new Leaf(Symbol.X),None),5)
    // assert(tree.depth == 5)
    // assert(List(Symbol.Up,Symbol.Right,Symbol.Down).equals(EvaluateOperation.evaluateOperationFromTree(tree)))
  }
  test("Check more binary operators") {
    // val andTreeIncorrect = new Node(Symbol.And,tree,Option(new Leaf(Symbol.Left)))
    // val andTreeCorrect = new Node(Symbol.And,tree,Option(new Leaf(Symbol.Right)))

    // assert(List().empty.equals(EvaluateOperation.evaluateOperationFromTree(andTreeIncorrect)))
    // assert(List(Symbol.Right).equals(EvaluateOperation.evaluateOperationFromTree(andTreeCorrect)))
  }

  test("Check operation evaluation") {
    /*val complexTree = SyntaxTreeBuilder.buildOperationFromComplexity(9)
    val stringTree  = complexTree.toString
    val result      = EvaluateOperation.evaluateOperationFromString(stringTree, List())
    print(stringTree, result)*/
  }
