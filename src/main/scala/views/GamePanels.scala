package views

import javax.swing.*
import java.awt.*

sealed trait GamePanels:
  def fastCalcPanel(): JPanel
  def countWordsPanel(): JPanel
  def rightDirectionsPanel(): JPanel

object GamePanels extends GamePanels, BaseView:
  /*function used to create simple panels for minigame which
  only require showing a question and checking the input inserted*/
  private def createSimpleQuestionAnswerGamePanel(question: String, inputLabel: String, validate: String => (String, Color)): JPanel =
    val newPanel = new JPanel()
    val questionArea = new JTextArea(question)
    val inputField = new JTextField(10)
    val feedbackLabel = new JLabel("", SwingConstants.CENTER) //TODO: use check image instead or string
    feedbackLabel.setPreferredSize(new Dimension(150, 30))
    questionArea.setEditable(false)
    questionArea.setFont(pixelFontBig)

    //pannello in alto in cui viene mostrato il quesito
    val questionPanel = new JPanel(new BorderLayout())
    questionPanel.add(questionArea, BorderLayout.CENTER)
    newPanel.add(questionPanel, BorderLayout.NORTH)

    def submit(): Unit =
      val input = inputField.getText.trim
      val (message, color) = validate(input)
      feedbackLabel.setText(message)
      feedbackLabel.setForeground(color)

    inputField.addActionListener(_ => submit())

    //pannello per input utente e feedback (al momento stringa)
    val inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10))
    inputPanel.add(new JLabel(inputLabel))
    inputPanel.add(inputField)
    inputPanel.add(feedbackLabel)

    newPanel.add(inputPanel, BorderLayout.CENTER)
    newPanel

  override def fastCalcPanel(): JPanel =
    val correctAnswer = "38" //TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = "11 + (3 x 9) = ",
      inputLabel = "Your result: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )

  override def countWordsPanel(): JPanel =
    val sentence = "This is a simple sentence"
    val correctAnswer = sentence.split("\\s+").count(_.nonEmpty).toString //TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = sentence,
      inputLabel = "Number of words: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )

  override def rightDirectionsPanel(): JPanel =
    val panel = new JPanel()
    panel.add(new JLabel("Right Directions panel"))
    //TODO: implement Right Directions panel here
    panel