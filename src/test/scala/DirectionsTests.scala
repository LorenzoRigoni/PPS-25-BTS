import models.rightDirections.DirectionsLogic
import models.rightDirections.Direction
import org.scalatest.funsuite.AnyFunSuite

class DirectionsTests extends AnyFunSuite:
  private val COMPLETE_LOGICAL_OPERATION = "(NOT UP) AND (NOT NOT DOWN)"
  private val INCOMPLETE_LOGICAL_OPERATION = "NOT UP     and NOT NOT DoWN"
  private val MULTIPLE_ANSWER_LOGICAL_OPERATION = "Not UP"

  test("A complete logical operation should always work") {
    assert(DirectionsLogic.checkValidity(COMPLETE_LOGICAL_OPERATION, Direction.fromString("down")))
  }

  test("An Incomplete logical operation (missing parenthesis or with incorrect formatting) " +
    "should be fixed by the trimmer and should always work") {
    assert(DirectionsLogic.checkValidity(COMPLETE_LOGICAL_OPERATION, Direction.fromString("down")))
  }

  test("An logical operation with multiple answers should nonetheless always work"){
    assert(DirectionsLogic.checkValidity(MULTIPLE_ANSWER_LOGICAL_OPERATION, Direction.fromString("down")))
  }
