package views.panels

import controllers.GameController
import views.*

import java.awt.*
import javax.swing.*

/**
 * This trait represents the views of the mini-games that have a question and an answer.
 */
trait SimpleQuestionAnswerGamePanel extends BaseView:

  /**
   * Create the panel with a question and an answer.
   * @param question
   *   the initial question of the mini-game
   * @param textInputLabel
   *   the label of the input
   * @param controller
   *   the game controller
   * @param onNext
   *   callback to notify when the mini-game ends
   * @param updateLogicAndQuestion
   *   callback that takes the controller and returns (updatedController, newQuestion) (i.e.,
   *   similar to `getQuestion`)
   * @param validate
   *   the function that returns (updatedController, isCorrect)
   * @param renderQuestionContent
   *
   * @return
   *   the panel created
   */
  def createSimpleQuestionAnswerGamePanel(
      question: String,
      textInputLabel: String,
      controller: GameController,
      onNext: GameController => Unit,
      updateLogicAndQuestion: GameController => (GameController, String),
      validate: (GameController, String) => (GameController, Boolean),
      renderQuestionContent: Option[JPanel => Unit] = None
  ): JPanel =
    val panel = new JPanel(new BorderLayout())

    val centerWrapper = new JPanel(new GridBagLayout())
    centerWrapper.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2))
    val innerPanel    = new JPanel()
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS))

    // Question area
    val questionArea = new JTextArea(question)
    questionArea.setEditable(false)
    questionArea.setFont(pixelFont15)
    questionArea.setOpaque(false)

    val questionWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER))
    questionWrapper.setOpaque(false)
    questionWrapper.add(questionArea)
    panel.add(questionWrapper, BorderLayout.NORTH)

    val customPanelWrapper = new JPanel()
    customPanelWrapper.setLayout(new GridBagLayout())
    customPanelWrapper.setOpaque(false)

    val gbc = new GridBagConstraints()
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.weightx = 1.0
    gbc.gridx = 0
    gbc.gridy = 0

    // if necessary a custom panel for rich questions (ex. ColoredCount)
    val customPanel = new JPanel()
    customPanel.setOpaque(false)
    renderQuestionContent.foreach(renderer => renderer(customPanel))
    customPanelWrapper.add(customPanel, gbc)
    panel.add(customPanelWrapper, BorderLayout.CENTER)

    // Input + Feedback
    val inputField    = new JTextField(10)
    val feedbackLabel = new JLabel("", SwingConstants.CENTER)
    feedbackLabel.setFont(pixelFont8)
    feedbackLabel.setPreferredSize(new Dimension(150, 30)) // TODO: avoid fixed size

    def showNewQuestion(newQuestion: String): Unit =
      questionArea.setText(newQuestion)
      inputField.setText("")
      feedbackLabel.setVisible(false)
      customPanel.removeAll()
      renderQuestionContent.foreach(renderer => renderer(customPanel))
      customPanel.revalidate()
      customPanel.repaint()

    var currentController = controller

    def submit(): Unit =
      val input                          = inputField.getText.trim
      val (updatedController, isCorrect) = validate(currentController, input)
      currentController = updatedController

      feedbackLabel.setVisible(true)
      feedbackLabel.setText(if isCorrect then "Correct!" else "Wrong!")
      feedbackLabel.setForeground(if isCorrect then Color.GREEN else Color.RED)

      if updatedController.isCurrentGameFinished then onNext(updatedController)
      else
        val (nextController, nextQuestion) = updateLogicAndQuestion(updatedController)
        currentController = nextController
        showNewQuestion(nextQuestion)

    inputField.addActionListener(_ => submit())

    val inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10))
    val inputLabel = new JLabel(textInputLabel)
    inputLabel.setFont(pixelFont8)
    inputPanel.add(inputLabel)
    inputPanel.add(inputField)
    inputPanel.add(feedbackLabel)
    panel.add(inputPanel, BorderLayout.SOUTH)

    panel
