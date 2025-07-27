package controllers

import utils.MiniGames

trait GameViewCallback:

  def onTimerUpdate(secondsLeft: Int): Unit
  def onGameChanged(miniGame: MiniGames, controller: GameController): Unit
  def onGameFinished(controller: GameController): Unit
