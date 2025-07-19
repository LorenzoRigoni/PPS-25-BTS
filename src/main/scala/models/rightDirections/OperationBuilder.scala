package models.rightDirections
import scala.util.Try
trait OperationBuilder:
  /**
   * Build an operation from an input.
   * Operation can fail if the input string doesn't
   * respect syntactical rules
   * @param input
   *  the input that will be checked for syntactical errors
   * @param formatter
   *  a function that formats correctly the input
   * @return
   *  the inputted string or a failure
   */
  def buildOperationFromString(input: String, formatter: (String)=>String): Try[String]
  /**
   * Build an operation from a given complexity.
   * Operation can fail if the input string doesn't
   * respect syntactical rules
   * @param base
   *  the starting point of the operation extraction
   * @param complexity
   *  each operation has a given complexity: the result
   *  should try to expand the base
   * @return
   * the expanded operation as string or a failure if base wasn't
   * well-formed
   */ 
  def buildOperationFromComplexity(base: String, complexity: Int): Try[String]

  /**
   * checks if a string is well-formed 
   * @param input
   *  the string which may or may not be well-formed
   * @return
   *  whether input is well-formed
   */
  def checkValidity(input: String): Boolean
