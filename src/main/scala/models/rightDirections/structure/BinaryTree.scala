package models.rightDirections.structure

trait BinaryTree[A] {
  def value: A
  def left: Option[BinaryTree[A]]
  def right: Option[BinaryTree[A]]
  def depth: Int

  def expand(target: A, leftValue: A, rightValue: A): BinaryTree[A]
}

