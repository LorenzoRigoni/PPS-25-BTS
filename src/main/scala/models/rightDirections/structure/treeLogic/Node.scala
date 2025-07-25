package models.rightDirections.structure.treeLogic
import models.rightDirections.structure.Symbol
class Node[A](val value: A, leftNode: BinaryTree[A], rightNode: Option[BinaryTree[A]])
    extends BinaryTree[A] {
  val left: Option[BinaryTree[A]]  = Some(leftNode)
  val right: Option[BinaryTree[A]] = rightNode
  val depth: Int                   = 1 + math.max(
    left.map(_.depth).getOrElse(0),
    right.map(_.depth).getOrElse(0)
  )

  override def expand(
      target: A,
      newValue: A,
      leftValue: Option[A],
      rightValue: Option[A]
  ): BinaryTree[A] = {
    if (value == target && left.isEmpty) {
      (leftValue, rightValue) match {
        case (None, None) =>
          new Leaf(newValue)
        case _            =>
          new Node(
            newValue,
            leftValue.map(new Leaf(_)).getOrElse(left.get),
            Option(rightValue.map(new Leaf(_)).getOrElse(right.get))
          )
      }
    } else {
      if (value == target) {
        return Node(newValue, left.get, None)
      }
      val expandedLeft  = left.get.expand(target, newValue, leftValue, rightValue)
      val expandedRight = right.map(_.expand(target, newValue, leftValue, rightValue)).orElse(None)
      left match {
        case Some(valueLeft) if expandedLeft != valueLeft =>
          Node(value, expandedLeft, right)
        case _                                            =>
          right match
            case Some(valueRight) if expandedRight.get != valueRight =>
              Node(value, left.get, expandedRight)
            case _                                                   => this
      }
    }
  }

  override def contains(value: A): Boolean =
    this.value == value || left.exists(_.contains(value)) || right.exists(_.contains(value))

  override def toString: String = {
    val leftStr  = left.fold("")(_.toString)
    val rightStr = right.fold("")(_.toString)

    (leftStr, rightStr) match {
      case ("", "") => value.toString
      case _        => s"($leftStr $value $rightStr)"
    }
  }
}
