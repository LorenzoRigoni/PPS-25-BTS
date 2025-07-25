import models.rightDirections.DirectionsLogic
import models.rightDirections.structure.{Symbol, *}
import models.rightDirections.structure.Symbol.{And, Not, Or, X}
import models.rightDirections.structure.treeLogic.{Leaf, Node}
import org.scalatest.funsuite.AnyFunSuite

class RightDirectionsTests extends AnyFunSuite:
  val tree = new Leaf(X)
  tree.expand(X, Symbol.Left, Option(Not), None)

  test("Check Not operator") {
    assert(tree.depth == 2)
    assert(
      List(Symbol.Up, Symbol.Right, Symbol.Down).equals(
        EvaluateOperation.evaluateOperationFromTree(tree)
      )
    )
  }
  test("Check more binary operators") {
    val andTreeIncorrect = new Node(Symbol.And, tree, Option(new Leaf(Symbol.Left)))
    val andTreeCorrect   = new Node(Symbol.And, tree, Option(new Leaf(Symbol.Right)))

    assert(List().empty.equals(EvaluateOperation.evaluateOperationFromTree(andTreeIncorrect)))
    assert(List(Symbol.Right).equals(EvaluateOperation.evaluateOperationFromTree(andTreeCorrect)))

  }
