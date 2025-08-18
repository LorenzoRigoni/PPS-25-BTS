package views.panels

import controllers.GameController
import utils.{ColoredCountQuestion, SimpleTextQuestion}

import javax.swing.*

/**
 * Factory trait for creating mini-games panels.
 */
sealed trait GamePanelsFactory:
  /**
   * Create a panel for Fast Calc mini-game.
   * @param controller
   *   the game controller managing the game logic
   * @param onNext
   *   callback invoked when the player completes the current question
   * @param question
   *   the question to display in the panel
   * @return
   *   the game panel for the mini-game
   */
  def fastCalcPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel

  /**
   * Create a panel for Count Words mini-game.
   * @param controller
   *   the game controller managing the game logic
   * @param onNext
   *   callback invoked when the player completes the current question
   * @param question
   *   the question to display in the panel
   * @return
   *   the game panel for the mini-game
   */
  def countWordsPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel

  /**
   * Create a panel for Right Directions mini-game.
   * @param controller
   *   the game controller managing the game logic
   * @param onNext
   *   callback invoked when the player completes the current question
   * @param question
   *   the question to display in the panel
   * @return
   *   the game panel for the mini-game
   */
  def rightDirectionsPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel

  /**
   * Create a panel for Word Memory mini-game.
   * @param controller
   *   the game controller managing the game logic
   * @param onNext
   *   callback invoked when the player completes the current question
   * @param question
   *   the question to display in the panel
   * @return
   *   the game panel for the mini-game
   */
  def wordMemoryPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel

  /**
   * Create a panel for Colored Count mini-game.
   * @param controller
   *   the game controller managing the game logic
   * @param onNext
   *   callback invoked when the player completes the current question
   * @param question
   *   the question to display in the panel
   * @return
   *   the game panel for the mini-game
   */
  def coloredCountPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: ColoredCountQuestion
  ): JPanel

class GamePanelsFactoryImpl extends GamePanelsFactory:
  override def fastCalcPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel =
    FastCalcPanel(controller, onNext, question).panel()

  override def countWordsPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel =
    CountWordsPanel(controller, onNext, question).panel()

  override def rightDirectionsPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel =
    RightDirectionsPanel(controller, onNext, question).panel()

  override def wordMemoryPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: SimpleTextQuestion
  ): JPanel =
    WordMemoryPanel(controller, onNext, question).panel()

  override def coloredCountPanel(
      controller: GameController,
      onNext: GameController => Unit,
      question: ColoredCountQuestion
  ): JPanel =
    ColoredCountPanel(controller, onNext, question).panel()
