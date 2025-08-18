package models.rightDirections.structure.treeLogic

import scala.util.Random

/**
 * Represents a generic binary tree node.
 *
 * @tparam A
 *   the type of the value stored in the tree nodes
 *
 * @define rng
 *   implicit random number generator used for operations involving randomness, external for testing
 *   and consistency purposes
 */
trait BinaryTree[A]:

  /**
   * The value stored in this node.
   */
  val value: A

  /**
   * The depth of this node in the tree.
   */
  val depth: Int

  /**
   * The optional left child node.
   */
  val left: Option[BinaryTree[A]]

  /**
   * The optional right child node.
   */
  val right: Option[BinaryTree[A]]

  /**
   * Expands the tree by finding the node containing target, and adding new nodes with values
   * newValue, leftValue, and rightValue as children, using the provided random generator.
   *
   * @param target
   *   the value of the node to expand
   * @param newValue
   *   the new value to add at the expansion point
   * @param leftValue
   *   optional value for the left child of the new node
   * @param rightValue
   *   optional value for the right child of the new node
   * @return
   *   a new binary tree with the expansion applied
   */
  def expand(target: A, newValue: A, leftValue: Option[A], rightValue: Option[A]): BinaryTree[A]

  /**
   * Returns a string representation of this binary tree node.
   *
   * @return
   *   string representation of the node and its children
   */
  def toString: String

  /**
   * Checks whether the tree contains a node with the specified value.
   *
   * @param value
   *   the value to search for
   * @return
   *   true if the value is found anywhere in the tree, false otherwise
   */
  def contains(value: A): Boolean
