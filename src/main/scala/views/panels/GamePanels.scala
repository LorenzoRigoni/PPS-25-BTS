package views.panels

import javax.swing.*

/**
 * This trait represents the possible mini-games panels.
 */
sealed trait GamePanels:
  /**
   * Create a panel for Fast Calc mini-game.
   * @return
   *   the created panel
   */
  def fastCalcPanel(): JPanel

  /**
   * Create a panel for Count Words mini-game.
   *
   * @return
   *   the created panel
   */
  def countWordsPanel(): JPanel

  /**
   * Create a panel for Right Directions mini-game.
   *
   * @return
   *   the created panel
   */
  def rightDirectionsPanel(): JPanel

object GamePanels extends GamePanels:
  override def fastCalcPanel(): JPanel        = FastCalcPanel.panel()
  override def countWordsPanel(): JPanel      = CountWordsPanel.panel()
  override def rightDirectionsPanel(): JPanel = RightDirectionsPanel.panel()
