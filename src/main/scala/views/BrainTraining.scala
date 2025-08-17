package views

import controllers.{GameController, GameViewCallback}
import utils.{ColoredCountQuestion, Question, SimpleTextQuestion}
import utils.enums.MiniGames.*
import views.panels.{
  GamePanelsFactory,
  GamePanelsFactoryImpl,
  ResultPanelsFactory,
  ResultPanelsFactoryImpl
}
import utils.constants.GUIConstants.*
import utils.enums.MiniGames
import views.panels.GamePanelMapper.*

import javax.swing.*
import java.awt.*

/**
 * Represents the view of the Brain Training mode. The user can freely choose and play any mini-game
 * to train their mind. * After completing a mini-game, the results are displayed in a result panel.
 * @param resultPanels
 *   a factory that provides result panels for displaying mini-game results
 */
case class BrainTraining(resultPanels: ResultPanelsFactory) extends GameViewCallback:
  private val GAME_BUTTON_W     = 300
  private val GAME_BUTTON_H     = 50
  private val frame             = new JFrame("Brain Training")
  private val buttonDimension   = new Dimension(GAME_BUTTON_W, GAME_BUTTON_H)
  private val mainPanel         = new JPanel(new BorderLayout())
  private val buttonPanel       = new JPanel()
  private val centerPanel       = new JPanel()
  private val bottomPanel       = new JPanel(new FlowLayout(FlowLayout.LEFT))
  private val initialController = GameController()

  /**
   * Show the brain training view with all available mini-games.
   * @param gamePanels
   *   the panel of the mini-game chosen
   */
  def show(gamePanels: GamePanelsFactory): Unit =
    frame.setBackground(Color.WHITE)
    UIHelper.centerFrame(frame, CENTER_FRAME_DIVISOR)
    val simplePanelMap = simpleTextPanelMap(gamePanels)
    centerPanel.setLayout(new BorderLayout())

    /**
     * Callback invoked after a mini-game is played
     * @param nextController
     *   the controller updated after answering the current question
     * @param miniGame
     *   the mini-game that was just played
     */
    def onNext(nextController: GameController, miniGame: MiniGames): Unit =
      if nextController.isCurrentGameFinished then onGameFinished(nextController)
      else loadGamePanel(nextController, miniGame)

    /**
     * Loads the panel of a specific mini-game with the current question
     * @param controller
     *   the current state of the game
     * @param miniGame
     *   the mini-game to be displayed
     */
    def loadGamePanel(controller: GameController, miniGame: MiniGames): Unit =
      val (newController, question) = controller.getQuestion
      val panel                     = (miniGame, question) match
        case (game, q: SimpleTextQuestion) if simplePanelMap.contains(game) =>
          simplePanelMap(game)(newController, onNext(_, miniGame), q)
        case (ColoredCount, q: ColoredCountQuestion)                        =>
          gamePanels.coloredCountPanel(newController, onNext(_, miniGame), q)
        case _                                                              =>
          throw new IllegalArgumentException("Type of question not handled")
      UIHelper.centerPanel(centerPanel, panel, Some(miniGame))
      mainPanel.remove(buttonPanel)

    /**
     * Creates a styled button for a mini-game and sets its behavior when clicked.
     * @param miniGame
     *   the mini-game associated with the button
     * @return
     *   a JButton that starts the mini-game when pressed
     */
    def createGameButton(miniGame: MiniGames): JButton =
      val button = UIHelper.createStyledButton(
        miniGame.displayName,
        buttonDimension,
        PIXEL_FONT15
      )
      button.addActionListener(_ => {
        val updatedController = initialController.chooseCurrentGame(miniGame)
        loadGamePanel(updatedController, miniGame)
      })
      button.setAlignmentX(Component.CENTER_ALIGNMENT)
      button
    MiniGames.values.foreach(miniGame =>
      buttonPanel.add(Box.createVerticalStrut(BUTTON_DISTANCE))
      buttonPanel.add(createGameButton(miniGame))
    )
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS))
    val backButton                                     =
      UIHelper.createStyledButton(
        "← Home",
        new Dimension(HOME_BUTTON_W, HOME_BUTTON_H),
        PIXEL_FONT8
      )
    backButton.addActionListener(_ => {
      frame.dispose()
      if mainPanel.isAncestorOf(buttonPanel) then MenuView.apply(GameController()).show()
      else BrainTraining.apply(ResultPanelsFactoryImpl()).show(GamePanelsFactoryImpl())
    })
    bottomPanel.add(backButton)
    mainPanel.add(buttonPanel, BorderLayout.NORTH)
    mainPanel.add(centerPanel, BorderLayout.CENTER)
    mainPanel.add(bottomPanel, BorderLayout.SOUTH)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit = ()

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      val numOfCorrectAnswers = controller.getNumberOfCorrectAnswers
      val numOfWrongAnswers   = controller.getNumberOfWrongAnswers
      val totalTime           = controller.getTotalTime
      val panel               =
        resultPanels.GameResultPanel(controller, numOfCorrectAnswers, numOfWrongAnswers, totalTime)
      mainPanel.remove(bottomPanel)
      UIHelper.centerPanel(centerPanel, panel, Option.empty)
    )
