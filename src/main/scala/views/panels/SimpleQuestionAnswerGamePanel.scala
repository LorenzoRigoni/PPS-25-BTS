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
  def panel(): JPanel

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
