package views.panels

import controllers.GameController
import views.*
import utils.{Question, SimpleTextQuestion}
import utils.constants.GUIConstants.*

import java.awt.*
import javax.swing.*

/**
 * This trait represents the views of the mini-games that need simple panel that only contains a
 * title, a question and a textfield for the answer.
 */
trait SimpleQuestionAnswerGamePanel[Q]:
  private val TEXTFIELD_COLS  = 40
  private val BORDER_VALUE    = 5
  private val FLOW_VALUE      = 10
  protected val inputField    = new JTextField(TEXTFIELD_COLS)
  protected val titleArea     = new JTextArea()
  protected val questionPanel = new JPanel()

  /**
   * Creates the Swing panel associated with this mini-game.
   * @return
   *   the constructed JPanel
   */
  def panel: JPanel

  /**
   * @param title
   *   the title displayed at the top of the panel
   * @param initialQuestion
   *   the initial question to render
   * @param textInputLabel
   *   the label describing the input field
   * @param controller
   *   the current GameController managing game state
   * @param onNext
   *   callback invoked when the user submits an answer
   * @param validate
   *   a function to check the submitted answer
   * @param renderQuestionContent
   *   optional custom renderer for displaying the question content
   * @return
   *   a tuple containing the constructed JPanel and a function
   */
  // TODO to complete function description in return
  def createSimpleQuestionAnswerGamePanel(
      title: String,
      initialQuestion: Q,
      textInputLabel: String,
      controller: GameController,
      onNext: GameController => Unit,
      validate: (GameController, String) => (GameController, Boolean),
      renderQuestionContent: Option[(JPanel, Q) => Unit] = None
  ): (JPanel, String => Unit) =
    val gbc                    = new GridBagConstraints()
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.weightx = 1.0
    gbc.gridx = 0
    gbc.gridy = 0
    val panel                  = new JPanel(new BorderLayout())
    val centerWrapper          = new JPanel(new GridBagLayout())
    centerWrapper.setBorder(
      BorderFactory.createEmptyBorder(
        BORDER_VALUE,
        BORDER_VALUE,
        BORDER_VALUE,
        BORDER_VALUE
      )
    )
    val innerPanel             = new JPanel()
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS))
    titleArea.setText(title)
    titleArea.setEditable(false)
    titleArea.setFont(PIXEL_FONT15)
    titleArea.setOpaque(false)
    val titleContainer         = new JPanel(new FlowLayout(FlowLayout.CENTER))
    titleContainer.setOpaque(false)
    titleContainer.add(titleArea)
    panel.add(titleContainer, BorderLayout.NORTH)
    val questionPanelContainer = new JPanel()
    questionPanelContainer.setLayout(new GridBagLayout())
    questionPanelContainer.setOpaque(false)
    questionPanel.setOpaque(false)
    renderQuestionContent.foreach(renderer => renderer(questionPanel, initialQuestion))
    questionPanelContainer.add(questionPanel, gbc)
    panel.add(questionPanelContainer, BorderLayout.CENTER)
    inputField.addActionListener(_ => submit(controller, onNext, validate, renderQuestionContent))
    val inputPanel             = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOW_VALUE, FLOW_VALUE))
    val inputLabel             = new JLabel(textInputLabel)
    inputLabel.setFont(PIXEL_FONT8)
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

  /**
   * Default renderer for simple text questions. Adds the question text into the provided container
   * JPanel.
   */
  protected def simpleLabelRenderer: (JPanel, Q) => Unit =
    (container, questionText) =>
      container.setLayout(new BorderLayout(BORDER_VALUE, BORDER_VALUE))
      val questionContent = questionText match
        case q: SimpleTextQuestion => q.text
      val question        = new JTextArea(questionContent)
      question.setFont(PIXEL_FONT25)
      question.setWrapStyleWord(true)
      question.setLineWrap(true)
      question.setEditable(false)
      question.setFocusable(false)
      container.setBorder(
        BorderFactory.createEmptyBorder(BORDER_VALUE, BORDER_VALUE, BORDER_VALUE, BORDER_VALUE)
      )
      container.add(question)
