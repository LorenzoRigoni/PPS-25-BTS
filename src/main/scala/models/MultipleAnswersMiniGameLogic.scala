package models

/**
 * Extended trait for mini-games that accept multiple correct answers.
 */
trait MultipleAnswersMiniGameLogic:
  /**
   * Generates a new question for the mini-game based on the specified difficulty level.
   * @return
   *   a new instance of the game logic and the question generated
   */
  def generateQuestion: (MultipleAnswersMiniGameLogic, String)

  /**
   * Method that calculates the percentage of correctness of the answer, instead of a simple
   * true/false result
   * @param question
   *   generated from the game logic
   * @param answer
   *   the user input
   * @return
   *   a score from 0.0 (completely wrong) to 1.0 (completely correct)
   */
  def evaluateAnswers(question: String, answer: String): Double
