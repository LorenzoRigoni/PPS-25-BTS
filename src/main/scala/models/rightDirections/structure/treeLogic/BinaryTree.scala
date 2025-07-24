package models.rightDirections.structure.treeLogic

trait BinaryTree[A] {
  val value: A
  val depth: Int

  val left: Option[BinaryTree[A]]
  val right: Option[BinaryTree[A]]

  def expand(target: A, newValue: A, leftValue: Option[A], rightValue: Option[A]): BinaryTree[A]
  def toString: String
  def contains(value: A): Boolean
}