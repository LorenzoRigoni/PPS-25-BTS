package views.panels

import controllers.GameController
import utils.constants.GUIConstants.TIMER_WORD_MEMORY
import utils.SimpleTextQuestion

import javax.swing.{JPanel, Timer}

class WordMemoryPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: SimpleTextQuestion
) extends SimpleQuestionAnswerGamePanel[SimpleTextQuestion]:
  def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Try to remember these words",
      question,
      "Write all words:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some(simpleLabelRenderer)
    )
    inputField.setEnabled(false)
    val timer      = new Timer(
      TIMER_WORD_MEMORY,
      _ =>
        questionPanel.removeAll()
        questionPanel.revalidate()
        questionPanel.repaint()
        inputField.setEnabled(true)
        inputField.requestFocusInWindow()
    )
    timer.setRepeats(false)
    timer.start()
    panel
