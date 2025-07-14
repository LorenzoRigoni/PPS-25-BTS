package views

import javax.swing.*
import java.awt.*
import java.awt.event.{KeyAdapter, KeyEvent, KeyListener}

sealed trait GamePanels:
  def fastCalcPanel(): JPanel
  def countWordsPanel(): JPanel
  def rightDirectionsPanel(): JPanel

object GamePanels extends GamePanels, BaseView:
  override def fastCalcPanel(): JPanel =
    val panel = new JPanel()

    val expressionArea = new JTextArea("11 + (3 x 9) = ")
    expressionArea.setEditable(false)
    expressionArea.setFont(new Font("Arial", Font.PLAIN, 24))

    val inputField = new JTextField(10)
    val feedbackLabel = new JLabel("", SwingConstants.CENTER) //TODO: use check image instead or string

    def submit(): Unit =
      val input = inputField.getText.trim
      val correctCount = "38" //TODO: temporary, logic still to implement

      val (message, color) =
        Option(input).filter(_ == correctCount)
          .map(_ => ("Correct!", Color.GREEN))
          .getOrElse((s"Wrong! The result was $correctCount", Color.RED))

      feedbackLabel.setText(message)
      feedbackLabel.setForeground(color)

    inputField.addActionListener(_ => submit())

    val inputPanel = new JPanel(new FlowLayout())
    inputPanel.add(new JLabel("Number of words:"))
    inputPanel.add(inputField)

    panel.add(expressionArea, BorderLayout.NORTH)
    panel.add(inputPanel, BorderLayout.CENTER)
    panel.add(feedbackLabel, BorderLayout.SOUTH)
    panel

  override def countWordsPanel(): JPanel =
    val panel = new JPanel()

    val sentenceArea = new JTextArea("This is a simple sentence")
    sentenceArea.setEditable(false)
    sentenceArea.setFont(new Font("Arial", Font.PLAIN, 16))

    val inputField = new JTextField(10)
    val feedbackLabel = new JLabel("", SwingConstants.CENTER)

    def submit(): Unit  =
      val input = inputField.getText.trim
      val correctCount = sentenceArea.getText.split("\\s+").count(_.nonEmpty).toString

      val (message, color) =
        Option(input).filter(_ == correctCount)
          .map(_ => ("Correct!", Color.GREEN))
          .getOrElse((s"Wrong! It was $correctCount words", Color.RED))

      feedbackLabel.setText(message)
      feedbackLabel.setForeground(color)

    inputField.addActionListener(_ => submit())

    val inputPanel = new JPanel(new FlowLayout())
    inputPanel.add(new JLabel("Number of words:"))
    inputPanel.add(inputField)

    panel.add(sentenceArea, BorderLayout.NORTH)
    panel.add(inputPanel, BorderLayout.CENTER)
    panel.add(feedbackLabel, BorderLayout.SOUTH)

    panel

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