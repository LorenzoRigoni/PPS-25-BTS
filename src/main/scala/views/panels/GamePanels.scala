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
  def fastCalcPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel

  /**
   * Create a panel for Count Words mini-game.
   *
   * @return
   *   the created panel
   */
  def countWordsPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel

  /**
   * Create a panel for Right Directions mini-game.
   *
   * @return
   *   the created panel
   */
  def rightDirectionsPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel

  def wordMemoryPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel

  def coloredCountPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel

class GamePanelsImpl extends GamePanels:
  override def fastCalcPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel =
    FastCalcPanel(controller, onNext, question).panel()

  override def countWordsPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel =
    CountWordsPanel(controller, onNext, question).panel()

  override def rightDirectionsPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: String
  ): JPanel =
    RightDirectionsPanel(controller, onNext, question).panel()

  override def wordMemoryPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel =
    WordMemoryPanel(controller, onNext, question).panel()

  override def coloredCountPanel(controller: GameController, onNext: GameController => Unit, question: String): JPanel =
    ColoredCountPanel(controller, onNext, question).panel()
