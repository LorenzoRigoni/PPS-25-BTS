package models

import utils.Question

/**
 * This is the basic logic of all the mini-games. Every mini-game must be able to generate the
 * question and validate the answer.
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
   * Check the answer of the user.
   * @param question
   *   the question to answer
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
