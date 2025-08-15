package views.panels

import controllers.GameController
import utils.{MiniGames, SimpleTextQuestion}
import utils.MiniGames.{CountWords, FastCalc, RightDirections, WordMemory}
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
