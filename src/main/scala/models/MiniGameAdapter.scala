package models

import utils.{MiniGames, Question}

/**
 * This class is an adapter of the mini-game logics. It is used from the controller.
 * @param logic
 *   The mini-game logic
 * @param getGameId
 *   The mini-game id
 * @param parser
 *   The parser for the user input
 * @tparam Q
 *   The type of question
 * @tparam A
 *   The type of answer
 * @tparam B
 *   The type of validation
 */
class MiniGameAdapter[Q <: Question, A, B](
    val logic: MiniGameLogic[Q, A, B],
    val getGameId: MiniGames,
    val parser: String => A
) extends MiniGameWrapper[Q, A, B]:
  override def generateQuestion: (MiniGameWrapper[Q, A, B], Q) =
    val (newLogic, question) = logic.generateQuestion
    (MiniGameAdapter(newLogic, getGameId, parser), question)

  override def validateAnswer(answer: A): B = logic.validateAnswer(answer)

  override def isMiniGameFinished: Boolean = logic.isMiniGameFinished

  override def parseAnswer(input: String): A = parser(input)
