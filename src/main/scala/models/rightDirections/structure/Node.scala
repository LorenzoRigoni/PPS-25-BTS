package models.rightDirections.structure
import models.rightDirections.structure

case class Node[A](value: A, leftNode: BinaryTree[A], rightNode: BinaryTree[A]) extends BinaryTree[A] {
  def left: Option[BinaryTree[A]] = Some(leftNode)
  def right: Option[BinaryTree[A]] = Some(rightNode)
  def depth: Int = 1 + math.max(leftNode.depth, rightNode.depth)

  def expand(target: A, leftValue: A, rightValue: A): BinaryTree[A] =
    Node(
      value,
      leftNode.expand(target, leftValue, rightValue),
      rightNode.expand(target, leftValue, rightValue)
    )
}
