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
  def fastCalcPanel(controller: GameController, onNext: GameController => Unit): JPanel

  /**
   * Create a panel for Count Words mini-game.
   *
   * @return
   *   the created panel
   */
  def countWordsPanel(controller: GameController, onNext: GameController => Unit): JPanel

  /**
   * Create a panel for Right Directions mini-game.
   *
   * @return
   *   the created panel
   */
  def rightDirectionsPanel(controller: GameController, onNext: GameController => Unit): JPanel

class GamePanelsImpl extends GamePanels:
  override def fastCalcPanel(controller: GameController, onNext: GameController => Unit): JPanel =
    val (updatedController, question) = controller.getQuestion
    FastCalcPanel(updatedController, question, onNext).panel()

  override def countWordsPanel(controller: GameController, onNext: GameController => Unit): JPanel =
    val (updatedController, question) = controller.getQuestion
    CountWordsPanel(updatedController, question, onNext).panel()

  override def rightDirectionsPanel(controller: GameController, onNext: GameController => Unit): JPanel =
    val (updatedController, question) = controller.getQuestion
    RightDirectionsPanel(updatedController, question, onNext).panel()
