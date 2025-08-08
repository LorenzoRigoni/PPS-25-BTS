package models

import utils.MiniGames

class MiniGameAdapter[A, B](logic: MiniGameLogic[A, B], val getGameId: MiniGames)
    extends MiniGameWrapper:
  override def generateQuestion: (MiniGameWrapper, String) =
    val (newLogic, question) = logic.generateQuestion
    (MiniGameAdapter(newLogic, getGameId), question)

  override def validateAnswer(answer: Any): Any = logic.validateAnswer(answer.asInstanceOf[A])

  override def isMiniGameFinished: Boolean = logic.isMiniGameFinished
