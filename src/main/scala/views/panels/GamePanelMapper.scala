package views.panels

import controllers.GameController
import models.SimpleTextQuestion
import utils.enums.MiniGames
import utils.enums.MiniGames.{CountWords, FastCalc, RightDirections, WordMemory}
import views.panels

import javax.swing.JPanel

/**
 * Object that maps mini-games of type SimpleTextQuestion to their game panel factory
 */
object GamePanelMapper:
  /**
   * Returns a mapping between mini-games of type SimpleTextQuestion and their corresponding panel
   * factories
   * @param gamePanels
   *   an instance of `GamePanels` containing the panel factories for each mini-game
   * @return
   *   a map from `MiniGames` enum values to their panel factory functions
   */
  def simpleTextPanelMap(
      gamePanels: GamePanelsFactory
  ): Map[MiniGames, (GameController, GameController => Unit, SimpleTextQuestion) => JPanel]        =
    Map(
      FastCalc        -> gamePanels.fastCalcPanel,
      CountWords      -> gamePanels.countWordsPanel,
      RightDirections -> gamePanels.rightDirectionsPanel,
      WordMemory      -> gamePanels.wordMemoryPanel
    )
