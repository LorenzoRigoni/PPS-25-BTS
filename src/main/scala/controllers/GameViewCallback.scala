package controllers

import utils.MiniGames

trait GameViewCallback:

  def onGameChanged(miniGame: MiniGames, controller: GameController): Unit
  def onGameFinished(controller: GameController): Unit
