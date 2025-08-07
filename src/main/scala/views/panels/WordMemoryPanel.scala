package views.panels

import controllers.GameController

import java.awt.FlowLayout
import javax.swing.{JLabel, JPanel}

class WordMemoryPanel(controller: GameController, onNext: GameController => Unit, question: String)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Try to remember these words",
      question,
      "Write all words:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input),
      Some(simpleLabelRenderer)
    )
    panel
