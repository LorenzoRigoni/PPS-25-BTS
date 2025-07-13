package views

import javax.swing.*
import java.awt.*

sealed trait GamePanels:
  def fastCalcPanel(): JPanel
  def countWordsPanel(): JPanel
  def rightDirectionsPanel(): JPanel

object GamePanels extends GamePanels:
  override def fastCalcPanel(): JPanel =
    val panel = new JPanel()
    panel.add(new JLabel("Fast Calc panel"))
    //TODO: implement Fast Calc panel here
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
    val panel = new JPanel()
    panel.add(new JLabel("Right Directions panel"))
    //TODO: implement Right Directions panel here
    panel