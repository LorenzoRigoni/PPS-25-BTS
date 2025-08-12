package models

import utils.{MiniGames, Question}

/**
 * This trait wraps the logic of all mini-games.
 *
 * @tparam Q
 *   The type of question
 * @tparam A
 *   The type of answer
 * @tparam B
 *   The type of validation
 */
trait MiniGameWrapper[Q <: Question, A, B]:
  /**
   * Generate a question.
   *
   * @return
   *   The question generated
   */
  def generateQuestion: (MiniGameWrapper[Q, A, B], Q)

  /**
   * Validate an answer.
   *
   * @param answer
   *   The answer inserted
   * @return
   *   the validation
   */
  def validateAnswer(answer: A): B

  /**
   * Check if the mini-game is finished.
   *
   * @return
   *   true if is it finished, false otherwise
   */
  def isMiniGameFinished: Boolean

  /**
   * Get the mini-game id.
   *
   * @return
   *   the mini-game id
   */
  def getGameId: MiniGames

  /**
   * Parse the answer of the user with the type required from the mini-game logic.
   *
   * @param input
   *   the answer to parse
   * @return
   *   the parsed answer
   */
  def parseAnswer(input: String): A
