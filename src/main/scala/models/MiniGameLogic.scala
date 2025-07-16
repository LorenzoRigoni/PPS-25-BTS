package models

/**
 * This is the basic logic of all the mini-games. Every mini-game must be able to generate the question and
 * validate the answer.
 */
trait MiniGameLogic:
  /**
   * Generate the question for the mini-game.
   * @param difficult the difficult of the question
   * @return the question generated
   */
  def generateQuestion(difficult: Int): String

  /**
   * Check the answer of the user.
   * @param question the question to answer
   * @param answer the user answer
   * @return true if the answer is correct, false otherwise
   */
  def validateAnswer(question: String, answer: Int): Boolean