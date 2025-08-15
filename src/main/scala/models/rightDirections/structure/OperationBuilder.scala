package models.rightDirections.structure

import models.rightDirections.structure.treeLogic.BinaryTree

trait OperationBuilder[A]:
  /**
   * Build an operation from a given complexity.
   * @param complexity
   *   each operation has a given complexity: the result should try to expand the base
   * @return
   *   the expanded operation as a Binary Tree
   */
  def buildOperationFromComplexity(complexity: Int): BinaryTree[A]

  /**
   * Build an operation from a given complexity.
   *
   * @param root
   *   the root of the tree on which to calculate the total complexity recursively
   * @return
   *   The complexity of the complete tree
   */
  def calculateTreeComplexity(root: BinaryTree[A]): Int

  /**
   * Build an operation from a given complexity.
   *
   * @param root
   *   the root of the tree to be expanded
   * @param complexity
   *   the desired complexity of the final tree
   * @return
   *   a BinaryTree of the correct complexity
   */
  def expandTree(root: BinaryTree[A], complexity: Int): BinaryTree[A]
