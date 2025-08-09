package views.panels

import controllers.GameController
import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Fast Calc.
 */
class FastCalcPanel(controller: GameController, onNext: GameController => Unit, question: String)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Solve the expression:",
      question,
      "Your result:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some(simpleLabelRenderer)
    )
    panel
