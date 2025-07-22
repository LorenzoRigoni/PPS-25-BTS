package models.rightDirections.structure.treeLogic
import models.rightDirections.structure.Symbol
class Node[A](val value: A, leftNode: BinaryTree[A], rightNode: Option[BinaryTree[A]]) extends BinaryTree[A] {
  val left: Option[BinaryTree[A]] = Some(leftNode)
  val right: Option[BinaryTree[A]] = rightNode
  val depth: Int = 1 + math.max(leftNode.depth, right.fold(0)(_.depth))

  override def expand(target: A, newValue: A, leftValue: Option[A], rightValue: Option[A]): BinaryTree[A] = {
    if (value == target && left.isEmpty) {
      (leftValue, rightValue) match {
        case (None, None) =>
          new Leaf(newValue)
        case _ =>
          new Node(
            newValue,
            leftValue.map(new Leaf(_)).getOrElse(left.get),
            Option(rightValue.map(new Leaf(_)).getOrElse(right.get))
          )
      }
    } else {
      val expandedLeft = left.get.expand(target, newValue, leftValue, rightValue)
      if (expandedLeft != left.get) {
        new Node(value, expandedLeft, right)
      } else if(value == target)  {
        new Node(newValue,left.get ,None)
      }
      else if(right.isDefined){
        val expandedRight = right.map(_.expand(target, newValue, leftValue, rightValue)).get
        new Node(value, left.get, Option(expandedRight))
      }
      else{
        this
      }
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
