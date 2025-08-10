package models

import utils.{MiniGames, Question}

trait MiniGameWrapper[Q <: Question, A, B]:
  def generateQuestion: (MiniGameWrapper[Q, A, B], Q)

  def validateAnswer(answer: A): B

  def isMiniGameFinished: Boolean

  def getGameId: MiniGames
  
  def parseAnswer(input: String): A
