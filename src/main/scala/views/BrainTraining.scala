package views

import controllers.{GameController, GameViewCallback}
import utils.{ColoredCountQuestion, MiniGames, Question, SimpleTextQuestion}
import utils.MiniGames.{ColoredCount, CountWords, FastCalc, RightDirections, WordMemory}
import views.panels.{GamePanels, GamePanelsImpl, ResultPanels, ResultPanelsImpl}
import utils.GUIConstants.*

import javax.swing.*
import java.awt.*

/**
 * This object represents the view of the training mode. In this mode, the user can train his mind
 * with all the mini-games.
 */
case class BrainTraining(resultPanels: ResultPanels) extends BaseView with GameViewCallback:
  private val frame             = new JFrame("Brain Training")
  private val buttonDimension   = new Dimension(300, 50)
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
    frame.setBackground(whiteColor)
    centerFrame(frame, 1.5)

    def loadGamePanel[Q <: Question](controller: GameController, miniGame: MiniGames): Unit =
      centerPanel.removeAll()
      val (newController, question) = controller.getQuestion

      def onNext(nextController: GameController): Unit =
        if nextController.isCurrentGameFinished then onGameFinished(nextController)
        else loadGamePanel(nextController, miniGame)

      val panel = (miniGame, question) match
        case (FastCalc | CountWords | RightDirections | WordMemory, q: SimpleTextQuestion) =>
          miniGame match
            case FastCalc        => gamePanels.fastCalcPanel(newController, onNext, q)
            case CountWords      => gamePanels.countWordsPanel(newController, onNext, q)
            case RightDirections => gamePanels.rightDirectionsPanel(newController, onNext, q)
            case WordMemory      => gamePanels.wordMemoryPanel(newController, onNext, q)
            case _               => throw new IllegalArgumentException("Type of question not handled")
        case (ColoredCount, q: ColoredCountQuestion)                                       =>
          gamePanels.coloredCountPanel(newController, onNext, q)
        case _ => throw new IllegalArgumentException("Type of question not handled")

      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
      mainPanel.remove(buttonPanel)

    centerPanel.setLayout(new BorderLayout())

    MiniGames.values.foreach(miniGame => {
      val button = createStyledButton(
        miniGame.displayName,
        buttonDimension,
        pixelFont15,
        customBlueColor,
        whiteColor
      )
      button.addActionListener(_ => {
        val updatedController = initialController.chooseCurrentGame(miniGame)
        loadGamePanel(updatedController, miniGame)
      })
      button.setAlignmentX(Component.CENTER_ALIGNMENT)
      buttonPanel.add(Box.createVerticalStrut(40))
      buttonPanel.add(button)
    })

    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS))

    val backButton =
      createStyledButton("← Home", new Dimension(100, 30), pixelFont8, customBlueColor, whiteColor)
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
      centerPanel.removeAll()
      mainPanel.remove(bottomPanel)
      val numOfCorrectAnswers = controller.getNumberOfCorrectAnswers
      val numOfWrongAnswers   = controller.getNumberOfWrongAnswers
      val totalTime           = controller.getTotalTime
      val panel               =
        resultPanels.GameResultPanel(controller, numOfCorrectAnswers, numOfWrongAnswers, totalTime)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
