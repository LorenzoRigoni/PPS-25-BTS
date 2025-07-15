package views.panels

import javax.swing.*
import java.awt.*

object RightDirectionsPanel extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val correctAnswer = "" //TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = "NOT RIGHT AND NOT NOT LEFT",
      textInputLabel = "Your answer: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )
    /*val mainPanel = new JPanel(new GridBagLayout())
    val squarePanel = new JPanel(new BorderLayout())
    squarePanel.setBackground(Color.LIGHT_GRAY)

    val directions = new JTextArea("NOT RIGHT AND NOT NOT LEFT") {
      setOpaque(false)
      setWrapStyleWord(true)
      setLineWrap(true)
      setEditable(false)
      setFocusable(false)
      setFont(pixelFontBig)
      setMargin(new Insets(20, 20, 20, 20))
    }

    squarePanel.add(directions, BorderLayout.CENTER)
    squarePanel.setPreferredSize(new Dimension(300, 300))
    mainPanel.add(squarePanel)

    mainPanel*/
