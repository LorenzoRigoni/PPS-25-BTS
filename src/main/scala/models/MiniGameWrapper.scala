package models

import utils.MiniGames

trait MiniGameWrapper:
  def generateQuestion: (MiniGameWrapper, String)

  def validateAnswer(answer: Any): Any

  def isMiniGameFinished: Boolean

  def getGameId: MiniGames
