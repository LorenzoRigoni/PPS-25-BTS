package views.panels

import controllers.GameController
import utils.ColoredCountQuestion
import utils.constants.GUIConstants.*
import utils.enums.ColoredCountColors

import java.awt.*
import javax.swing.*

/**
 * Class used to create the panel for the game "Colored Count"
 * @param controller
 *   the controller that manages the game logic and state
 * @param onNext
 *   callback invoked when the player completes the current question
 * @param question
 *   the question to display
 */
class ColoredCountPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: ColoredCountQuestion
) extends SimpleQuestionAnswerGamePanel[ColoredCountQuestion]:
  override def panel(): JPanel =
    val questionText = s"How many ${question.colorRequired.toString.toLowerCase} numbers?"
    val (panel, _)   = createSimpleQuestionAnswerGamePanel(
      questionText,
      question,
      "Your answer:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some((container, q) => renderNumbers(container, q.numbersWithColor))
    )
    panel

  private def renderNumbers(container: JPanel, numbersPart: Seq[(Int, ColoredCountColors)]): Unit =
    container.setLayout(new FlowLayout())
    numbersPart.foreach((num, color) =>
      val label = new JLabel(num.toString)
      label.setForeground(color.color)
      label.setFont(PIXEL_FONT25)
      container.add(label)
    )
