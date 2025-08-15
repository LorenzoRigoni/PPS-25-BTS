package views.panels

import controllers.GameController
import utils.SimpleTextQuestion
import utils.enums.MiniGames
import utils.enums.MiniGames.{CountWords, FastCalc, RightDirections, WordMemory}
import views.panels

import javax.swing.JPanel

object GamePanelMapper:
  def simpleTextPanelMap(
      gamePanels: GamePanels
  ): Map[MiniGames, (GameController, GameController => Unit, SimpleTextQuestion) => JPanel]        =
    Map(
      FastCalc        -> gamePanels.fastCalcPanel,
      CountWords      -> gamePanels.countWordsPanel,
      RightDirections -> gamePanels.rightDirectionsPanel,
      WordMemory      -> gamePanels.wordMemoryPanel
    )
