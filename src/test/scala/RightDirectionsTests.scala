import models.rightDirections.DirectionsLogic
import models.rightDirections.structure.{Symbol, SyntaxTreeBuilder}
import org.scalatest.funsuite.AnyFunSuite

class RightDirectionsTests extends AnyFunSuite:
  private val COMPLETE_LOGICAL_OPERATION = "(NOT UP) AND (NOT NOT DOWN)"
  private val INCOMPLETE_LOGICAL_OPERATION = "NOT UP     and NOT NOT DoWN"
  private val MULTIPLE_ANSWER_LOGICAL_OPERATION = "Not UP"

  test("test"){
    val a = SyntaxTreeBuilder.buildOperationFromComplexity(3)
    print(a)
    assert(a.depth == 3)
  }
