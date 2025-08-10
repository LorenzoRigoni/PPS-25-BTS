package views.panels

import controllers.GameController
import utils.SimpleTextQuestion

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Count Words.
 */
class CountWordsPanel(controller: GameController, onNext: GameController => Unit, question: SimpleTextQuestion)
    extends SimpleQuestionAnswerGamePanel[SimpleTextQuestion]:
  def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Count the words",
      question,
      "Number of words:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some(simpleLabelRenderer)
    )
    panel
