package views.panels

import controllers.GameController
import views.*
import utils.{Question, SimpleTextQuestion}
import utils.GUIConstants.*

import java.awt.*
import javax.swing.*

/**
 * This trait represents the views of the mini-games that have a question and an answer.
 */
trait SimpleQuestionAnswerGamePanel[Q] extends BaseView:

  protected val inputField    = new JTextField(40)
  protected val titleArea     = new JTextArea()
  protected val questionPanel = new JPanel()

  /**
   * Create the panel with a question and an answer.
   * @param title
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
      title: String,
      initialQuestion: Q,
      textInputLabel: String,
      controller: GameController,
      onNext: GameController => Unit,
      validate: (GameController, String) => (GameController, Boolean),
      renderQuestionContent: Option[(JPanel, Q) => Unit] = None
  ): (JPanel, String => Unit) =
    val panel         = new JPanel(new BorderLayout())
    val centerWrapper = new JPanel(new GridBagLayout())
    centerWrapper.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2))
    val innerPanel    = new JPanel()
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS))

    // Title area
    titleArea.setText(title)
    titleArea.setEditable(false)
    titleArea.setFont(pixelFont15)
    titleArea.setOpaque(false)

    val titleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER))
    titleContainer.setOpaque(false)
    titleContainer.add(titleArea)
    panel.add(titleContainer, BorderLayout.NORTH)

    val questionPanelContainer = new JPanel()
    questionPanelContainer.setLayout(new GridBagLayout())
    questionPanelContainer.setOpaque(false)

    val gbc = new GridBagConstraints()
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.weightx = 1.0
    gbc.gridx = 0
    gbc.gridy = 0

    questionPanel.setOpaque(false)
    renderQuestionContent.foreach(renderer => renderer(questionPanel, initialQuestion))
    questionPanelContainer.add(questionPanel, gbc)
    panel.add(questionPanelContainer, BorderLayout.CENTER)

    inputField.addActionListener(_ => submit(controller, onNext, validate, renderQuestionContent))

    val inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10))
    val inputLabel = new JLabel(textInputLabel)
    inputLabel.setFont(pixelFont8)
    inputPanel.add(inputLabel)
    inputPanel.add(inputField)
    panel.add(inputPanel, BorderLayout.SOUTH)

    (
      panel,
      (input: String) => {
        submit(controller, onNext, validate, renderQuestionContent)
      }
    )

  private def submit(
      currentController: GameController,
      onNext: GameController => Unit,
      validate: (GameController, String) => (GameController, Boolean),
      renderQuestionContent: Option[(JPanel, Q) => Unit] = None
  ): GameController =
    val input                          = inputField.getText.trim
    val (updatedController, isCorrect) = validate(currentController, input)
    onNext(updatedController)
    updatedController

  protected def showNewQuestion(
      newQuestion: Q,
      renderQuestionContent: Option[(JPanel, Q) => Unit] = None
  ): Unit =
    inputField.setText("")
    questionPanel.removeAll()
    renderQuestionContent.foreach(renderer => renderer(questionPanel, newQuestion))
    questionPanel.revalidate()
    questionPanel.repaint()

  protected def simpleLabelRenderer: (JPanel, Q) => Unit =
    (container, questionText) =>
      container.setLayout(new BorderLayout(10, 10))
      val questionContent = questionText match
        case q: SimpleTextQuestion => q.text
      val question = new JTextArea(questionContent)
      question.setFont(pixelFont25)
      question.setWrapStyleWord(true)
      question.setLineWrap(true)
      question.setEditable(false)
      question.setFocusable(false)
      container.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 10))
      container.add(question)
