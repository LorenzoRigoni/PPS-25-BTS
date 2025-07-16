package views.panels

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
   *   the question of the mini-game
   * @param textInputLabel
   *   the label of the input
   * @param validate
   *   the function to validate the answer
   * @return
   *   the panel created
   */
  def createSimpleQuestionAnswerGamePanel(
      question: String,
      textInputLabel: String,
      validate: String => (String, Color)
  ): JPanel =
    val panel = new JPanel(new BorderLayout())

    val centerWrapper = new JPanel(new GridBagLayout())
    centerWrapper.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2))
    val innerPanel    = new JPanel()
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS))

    // Question area
    val questionArea = new JTextArea(question)
    questionArea.setEditable(false)
    questionArea.setFont(pixelFont25)
    questionArea.setWrapStyleWord(true)
    questionArea.setLineWrap(true)
    questionArea.setAlignmentX(Component.CENTER_ALIGNMENT)
    questionArea.setOpaque(false)

    val questionPanel = new JPanel()
    questionPanel.setOpaque(false)
    questionPanel.setLayout(new BorderLayout())
    questionPanel.add(questionArea, BorderLayout.CENTER)

    // Input + Feedback
    val inputField    = new JTextField(10)
    val feedbackLabel = new JLabel("", SwingConstants.CENTER)
    feedbackLabel.setFont(pixelFont8)
    feedbackLabel.setPreferredSize(new Dimension(150, 30)) // TODO: avoid fixed size

    def submit(): Unit =
      val input            = inputField.getText.trim
      val (message, color) = validate(input)
      feedbackLabel.setText(message)
      feedbackLabel.setForeground(color)

    inputField.addActionListener(_ => submit())

    val inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10))
    val inputLabel = new JLabel(textInputLabel)
    inputLabel.setFont(pixelFont8)
    inputPanel.add(inputLabel)
    inputPanel.add(inputField)
    inputPanel.add(feedbackLabel)
    inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT)

    innerPanel.add(questionPanel)
    innerPanel.add(Box.createVerticalStrut(20))
    innerPanel.add(inputPanel)

    centerWrapper.add(innerPanel, new GridBagConstraints())
    panel.add(centerWrapper, BorderLayout.CENTER)

    // Bottom panel with Back button
    val backButton =
      createStyledButton("← Home", new Dimension(100, 30), pixelFont8, customBlueColor, whiteColor)
    backButton.addActionListener(_ => BrainTraining.show(GamePanels))

    val bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT))
    bottomPanel.add(backButton)
    panel.add(bottomPanel, BorderLayout.SOUTH)
    panel
