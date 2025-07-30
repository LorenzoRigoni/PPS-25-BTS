package models.rightDirections.structure.treeLogic

class Leaf[A](val value: A) extends BinaryTree[A] {
  val left: Option[BinaryTree[A]]  = None
  val right: Option[BinaryTree[A]] = None
  val depth                        = 1

  override def expand(
      target: A,
      newValue: A,
      leftValue: Option[A],
      rightValue: Option[A]
  ): BinaryTree[A] =
    if (target != value) this
    else {
      (leftValue, rightValue) match {
        case (None, None) =>
          new Leaf(newValue)

        case (_, None) =>
          new Node(newValue, new Leaf(target), None)

        case _ =>
          new Node(newValue, new Leaf(target), Option(new Leaf(target)))
      }
    }

  override def contains(value: A): Boolean = this.value == value

  override def toString: String =
    value.toString
}
