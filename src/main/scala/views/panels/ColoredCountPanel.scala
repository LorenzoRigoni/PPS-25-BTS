package views.panels

import controllers.GameController
import utils.GUIConstants.*

import java.awt.*
import javax.swing.*

class ColoredCountPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: String
) extends SimpleQuestionAnswerGamePanel:

  def panel(): JPanel =
    val Array(numbersPart, targetColor) = question.split("\\|").map(_.trim)
    val questionText                    = s"How many $targetColor numbers?"
    val (panel, _)                      = createSimpleQuestionAnswerGamePanel(
      questionText,
      question,
      "Your answer:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some((container, _) => renderNumbers(container, numbersPart))
    )
    panel

  override protected def showNewQuestion(
      newQuestion: String,
      renderQuestionContent: Option[(JPanel, String) => Unit]
  ): Unit =
    val Array(numbersPart, targetColor) = newQuestion.split("\\|").map(_.trim)
    titleArea.setText(s"How many $targetColor numbers?")
    inputField.setText("")
    questionPanel.removeAll()
    renderNumbers(questionPanel, numbersPart)
    questionPanel.revalidate()
    questionPanel.repaint()

  private def renderNumbers(container: JPanel, numbersPart: String): Unit =
    container.setLayout(new FlowLayout())
    numbersPart
      .split("\\s+")
      .toList
      .map(pair => {
        val Array(num, colorName) = pair.split(":")
        val color                 = colorName.toUpperCase match
          case "RED"    => Color.RED
          case "BLUE"   => Color.BLUE
          case "GREEN"  => Color.GREEN
          case "YELLOW" => Color.YELLOW
          case _        => Color.BLACK

        val label = new JLabel(num)
        label.setForeground(color)
        label.setFont(pixelFont25)
        label
      })
      .foreach(container.add)
