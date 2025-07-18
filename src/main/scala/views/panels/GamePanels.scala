package views.panels

import controllers.GameController

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

class GamePanelsImpl(controller: GameController) extends GamePanels:
  override def fastCalcPanel(): JPanel =
    val question = controller.getQuestion
    FastCalcPanel(controller.copy(lastQuestion = Some(question)), question).panel()
  override def countWordsPanel(): JPanel =
    val question = controller.getQuestion
    CountWordsPanel(controller.copy(lastQuestion = Some(question)), question).panel()
  override def rightDirectionsPanel(): JPanel =
    val question = controller.getQuestion
    RightDirectionsPanel(controller.copy(lastQuestion = Some(question)), question).panel()