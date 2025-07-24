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
    if (value == target) {
      (leftValue, rightValue) match {
        case (None, None) =>
          new Leaf(newValue)
        case (_, None)    =>
          new Node(
            newValue,
            leftValue.map(v => new Leaf(v)).getOrElse(new Leaf(value)),
            None
          )
        case _            =>
          new Node(
            newValue,
            leftValue.map(v => new Leaf(v)).getOrElse(new Leaf(value)),
            Option(rightValue.map(v => new Leaf(v)).getOrElse(new Leaf(value)))
          )
      }
    } else {
      this
    }

  override def contains(value: A): Boolean = this.value == value

  override def toString: String =
    value.toString
}
