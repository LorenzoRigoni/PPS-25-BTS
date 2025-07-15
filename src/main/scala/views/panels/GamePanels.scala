package views.panels

import javax.swing.*

sealed trait GamePanels:
  def fastCalcPanel(): JPanel
  def countWordsPanel(): JPanel
  def rightDirectionsPanel(): JPanel

object GamePanels extends GamePanels:
  override def fastCalcPanel(): JPanel = FastCalcPanel.panel()
  override def countWordsPanel(): JPanel = CountWordsPanel.panel()
  override def rightDirectionsPanel(): JPanel = RightDirectionsPanel.panel()