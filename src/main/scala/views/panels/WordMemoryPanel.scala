package views.panels

import controllers.GameController

import javax.swing.{JPanel, Timer}

class WordMemoryPanel(controller: GameController, onNext: GameController => Unit, question: String)
    extends SimpleQuestionAnswerGamePanel:
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
      30000,
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
