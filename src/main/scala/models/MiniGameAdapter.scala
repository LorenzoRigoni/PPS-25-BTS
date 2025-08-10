package models

import utils.{MiniGames, Question}

class MiniGameAdapter[Q <: Question, A, B](
    logic: MiniGameLogic[Q, A, B],
    val getGameId: MiniGames,
    val parser: String => A
) extends MiniGameWrapper[Q, A, B]:
  override def generateQuestion: (MiniGameWrapper[Q, A, B], Q) =
    val (newLogic, question) = logic.generateQuestion
    (MiniGameAdapter(newLogic, getGameId, parser), question)

  override def validateAnswer(answer: A): B = logic.validateAnswer(answer)

  override def isMiniGameFinished: Boolean = logic.isMiniGameFinished

  override def parseAnswer(input: String): A = parser(input)
