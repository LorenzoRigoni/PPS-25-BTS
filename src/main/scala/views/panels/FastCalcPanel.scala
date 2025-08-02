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
    createSimpleQuestionAnswerGamePanel(
      question,
      "Your result: ",
      controller,
      onNext,
      _.getQuestion,
      (ctrl, input) => ctrl.checkAnswer(input)
    )
