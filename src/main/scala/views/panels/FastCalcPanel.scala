package views.panels

import controllers.GameController
import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Fast Calc.
 */
class FastCalcPanel(controller: GameController, question: String, onNext: GameController => Unit)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Solve the expression:",
      "Your result: ",
      controller,
      onNext,
      _.getQuestion,
      (ctrl, input) => ctrl.checkAnswer(input),
      Some(container => {
        container.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10))
        val lbl = new JLabel(question)
        lbl.setFont(pixelFont25)
        container.add(lbl)
      })
    )
    panel
