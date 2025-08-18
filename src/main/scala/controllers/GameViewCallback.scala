package controllers

import utils.enums.MiniGames

/**
 * This trait represents the callback used by the controller to notify the view of an event
 * occurred.
 */
trait GameViewCallback:

  /**
   * Handle the change of the mini-game.
   *
   * @param miniGame
   *   The new mini-game
   * @param controller
   *   The new controller
   */
  def onGameChanged(miniGame: MiniGames, controller: GameController): Unit

  /**
   * Handle the end of the test/game.
   *
   * @param controller
   *   The final controller
   */
  def onGameFinished(controller: GameController): Unit
