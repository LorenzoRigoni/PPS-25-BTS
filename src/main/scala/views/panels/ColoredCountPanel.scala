package views.panels

import controllers.GameController
import utils.ColoredCountQuestion
import utils.constants.GUIConstants.*
import utils.enums.ColoredCountColors

import java.awt.*
import javax.swing.*

class ColoredCountPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: ColoredCountQuestion
) extends SimpleQuestionAnswerGamePanel[ColoredCountQuestion]:

  def panel(): JPanel =
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

  override protected def showNewQuestion(
      newQuestion: ColoredCountQuestion,
      renderQuestionContent: Option[(JPanel, ColoredCountQuestion) => Unit]
  ): Unit =
    titleArea.setText(s"How many ${question.colorRequired.toString.toLowerCase} numbers?")
    inputField.setText("")
    questionPanel.removeAll()
    renderNumbers(questionPanel, newQuestion.numbersWithColor)
    questionPanel.revalidate()
    questionPanel.repaint()

  private def renderNumbers(container: JPanel, numbersPart: Seq[(Int, ColoredCountColors)]): Unit =
    container.setLayout(new FlowLayout())
    numbersPart.foreach((num, color) =>
      val label = new JLabel(num.toString)
      label.setForeground(color.color)
      label.setFont(PIXEL_FONT25)
      container.add(label)
    )
