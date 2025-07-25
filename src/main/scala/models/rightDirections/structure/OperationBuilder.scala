package models.rightDirections.structure

import models.rightDirections.structure.treeLogic.BinaryTree

trait OperationBuilder[A]:
  /**
   * Build an operation from an input. Operation can fail if the input string doesn't respect
   * syntactical rules
   * @param input
   *   the input that will be checked for syntactical errors
   * @param formatter
   *   a function that formats correctly the input
   * @return
   *   the inputted string or a failure
   */
  def buildOperationFromString(input: String, formatter: (String) => String): Option[BinaryTree[A]]

  /**
   * Build an operation from a given complexity.
   * @param complexity
   *   each operation has a given complexity: the result should try to expand the base
   * @return
   *   the expanded operation as a Binary Tree
   */
  def buildOperationFromComplexity(complexity: Int): BinaryTree[A]

  /**
   * checks if a string is well-formed
   * @param input
   *   the string which may or may not be well-formed
   * @return
   *   a well-formed version of input
   */
  protected def formatString(input: String): String
