package models.rightDirections.structure.treeLogic
import models.rightDirections.structure.Symbol
class Node[A](val value: A, leftNode: BinaryTree[A], rightNode: Option[BinaryTree[A]]) extends BinaryTree[A] {
  val left: Option[BinaryTree[A]] = Some(leftNode)
  val right: Option[BinaryTree[A]] = rightNode
  val depth: Int = 1 + math.max(
    left.map(_.depth).getOrElse(0),
    right.map(_.depth).getOrElse(0)
  )

  override def expand(target: A, newValue: A, leftValue: Option[A], rightValue: Option[A]): BinaryTree[A] = {
    if( value==target)
      return new Node(newValue, this, None)

    val expandedLeft = left.get.expand(target, newValue, leftValue, rightValue)

    if (right.isDefined) {
      val expandedRight = right.get.expand(target, newValue, leftValue, rightValue)
      expandBothBranchNode(expandedLeft, expandedRight)
    }
    else
      Node(value, expandedLeft, None)
  }

  private def expandBothBranchNode(expandedLeft: BinaryTree[A], expandedRight: BinaryTree[A]): BinaryTree[A] = {

    (expandedLeft, expandedRight) match {
      case (l, r) if !l.equals(left.get) && !r.equals(right.get) =>
        if (scala.util.Random.nextBoolean())
          Node(value, expandedLeft, right)
        else
          Node(value, left.get, Option(expandedRight))

      case (l, r) if l != left.get =>
        Node(value, expandedLeft, right)

      case _ =>
        Node(value, left.get, Option(expandedRight))
    }
  }


  override def contains(value: A): Boolean =
    this.value == value || left.exists(_.contains(value)) || right.exists(_.contains(value))

  override def toString: String = {
    val leftStr = left.fold("")(_.toString)
    val rightStr = right.fold("")(_.toString)

    (leftStr, rightStr) match {
      case ("", "") => value.toString
      case _ => s"($leftStr $value $rightStr)"
    }
  }
}
