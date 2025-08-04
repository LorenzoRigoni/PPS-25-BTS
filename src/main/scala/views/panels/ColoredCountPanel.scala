package views.panels

import controllers.GameController

import java.awt.*
import javax.swing.*

class ColoredCountPanel(
    controller: GameController,
    question: String,
    onNext: GameController => Unit
) extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val Array(numbersPart, targetColor) = question.split("\\|").map(_.trim)

    val data = numbersPart.split("\\s+").toList.map { pair =>
      val Array(num, colorName) = pair.split(":")
      val color                 = colorName.toUpperCase match
        case "RED"    => Color.RED
        case "BLUE"   => Color.BLUE
        case "GREEN"  => Color.GREEN
        case "YELLOW" => Color.YELLOW
        case _        => Color.BLACK
      (num, color)
    }

    val questionText = s"How many $targetColor numbers?"

    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      questionText,
      "Your answer:",
      controller,
      onNext,
      _.getQuestion,
      (ctrl, input) => ctrl.checkAnswer(input),
      Some(container => {
        container.setLayout(new FlowLayout())
        for (text, color) <- data do
          val label = new JLabel(text)
          label.setForeground(color)
          label.setFont(pixelFont25)
          container.add(label)
      })
    )
    panel
