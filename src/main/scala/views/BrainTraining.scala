package views

import controllers.{GameController, GameViewCallback}
import utils.{ColoredCountQuestion, MiniGames, Question, SimpleTextQuestion}
import utils.MiniGames.*
import views.panels.{GamePanels, GamePanelsImpl, ResultPanels, ResultPanelsImpl}
import utils.GUIConstants.*
import views.panels.GamePanelMapper.*

import javax.swing.*
import java.awt.*

/**
 * This object represents the view of the training mode. In this mode, the user can train his mind
 * with all the mini-games.
 */
case class BrainTraining(resultPanels: ResultPanels) extends GameViewCallback:
  private val frame             = new JFrame("Brain Training")
  private val gameButtonW       = 300
  private val gameButtonH       = 50
  private val buttonDimension   = new Dimension(gameButtonW, gameButtonH)
  private val mainPanel         = new JPanel(new BorderLayout())
  private val buttonPanel       = new JPanel()
  private val centerPanel       = new JPanel()
  private val bottomPanel       = new JPanel(new FlowLayout(FlowLayout.LEFT))
  private val initialController = GameController()

  /**
   * Show the brain training view with a mini-game.
   *
   * @param gamePanels
   *   the panel of the mini-game chose
   */
  def show(gamePanels: GamePanels): Unit =
    frame.setBackground(Color.WHITE)
    UIHelper.centerFrame(frame, CENTER_FRAME_DIVISOR)
    val simplePanelMap = simpleTextPanelMap(gamePanels)
    centerPanel.setLayout(new BorderLayout())

    def onNext(nextController: GameController, miniGame: MiniGames): Unit =
      if nextController.isCurrentGameFinished then onGameFinished(nextController)
      else loadGamePanel(nextController, miniGame)

    def loadGamePanel(controller: GameController, miniGame: MiniGames): Unit =
      val (newController, question) = controller.getQuestion
      val panel                     = (miniGame, question) match
        case (game, q: SimpleTextQuestion) if simplePanelMap.contains(game) =>
          simplePanelMap(game)(newController, onNext(_, miniGame), q)
        case (ColoredCount, q: ColoredCountQuestion)                        =>
          gamePanels.coloredCountPanel(newController, onNext(_, miniGame), q)
        case _                                                              =>
          throw new IllegalArgumentException("Type of question not handled")
      UIHelper.centerPanel(centerPanel, panel)
      mainPanel.remove(buttonPanel)

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

    val backButton =
      UIHelper.createStyledButton(
        "← Home",
        new Dimension(HOME_BUTTON_W, HOME_BUTTON_H),
        PIXEL_FONT8
      )
    backButton.addActionListener(_ => {
      frame.dispose()
      if mainPanel.isAncestorOf(buttonPanel) then MenuView.apply(GameController()).show()
      else BrainTraining.apply(ResultPanelsImpl()).show(GamePanelsImpl())
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
      UIHelper.centerPanel(centerPanel, panel)
    )
