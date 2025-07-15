package views

import javax.swing.*
import java.awt.*
import java.awt.event.{KeyAdapter, KeyEvent, KeyListener}

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
    val mainPanel = new JPanel(new GridBagLayout()) {
      setFocusable(true)
      requestFocusInWindow()
      addKeyListener(new KeyAdapter {
        override def keyPressed(e: KeyEvent): Unit = e.getKeyCode match {
          case KeyEvent.VK_W => println("Up (W) pressed")
          case KeyEvent.VK_A => println("Left (A) pressed")
          case KeyEvent.VK_S => println("Down (S) pressed")
          case KeyEvent.VK_D => println("Right (D) pressed")
          case _ =>
        }
      })
    }

    val squarePanel = new JPanel(new BorderLayout())
    squarePanel.setBackground(Color.LIGHT_GRAY)

    val directions = new JTextArea("NOT RIGHT AND NOT NOT LEFT") {
      setOpaque(false)
      setWrapStyleWord(true)
      setLineWrap(true)
      setEditable(false)
      setFocusable(false)
      setFont(new Font("Arial", Font.PLAIN, 40))
      setMargin(new Insets(20, 20, 20, 20))
    }

    val textWrapper = new JPanel(new GridBagLayout()) {
      setOpaque(false)
      add(directions, new GridBagConstraints() {
        gridx = 0
        gridy = 0
        weightx = 1.0
        weighty = 1.0
        fill = GridBagConstraints.BOTH
        anchor = GridBagConstraints.CENTER
      })
    }

    squarePanel.add(directions, BorderLayout.CENTER)

    squarePanel.setPreferredSize(new Dimension(300, 300))

    mainPanel.add(squarePanel)
    mainPanel