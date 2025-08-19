package models

/**
 * This is the basic logic of all the mini-games. Every mini-game must be able to generate the
 * question and validate the answer.
 *
 * @tparam Q
 *   The type of question
 * @tparam A
 *   The type of answer
 * @tparam B
 *   The type of validation
 */
trait MiniGameLogic[Q <: Question, A, B]:
  /**
   * Generate the question for the mini-game.
   *
   * @return
   *   a copy of the game logic and the question generated
   */
  def generateQuestion: (MiniGameLogic[Q, A, B], Q)

  /**
   * Parse the answer of the user in the type required from the mini-game.
   *
   * @param answer
   *   The user answer
   * @return
   *   an Option with the parsed answer if is it possible
   */
  def parseAnswer(answer: String): Option[A]

  /**
   * Check the answer of the user.
   * @param answer
   *   the user answer
   * @return
   *   true if the answer is correct, false otherwise
   */
  def validateAnswer(answer: A): B

  /**
   * Check if the mini-game is finished.
   *
   * @return
   *   true if the game is finished, false otherwise
   */
  def isMiniGameFinished: Boolean
