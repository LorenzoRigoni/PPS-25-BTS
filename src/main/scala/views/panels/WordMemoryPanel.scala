package views.panels

import controllers.GameController

import java.awt.FlowLayout
import javax.swing.{JLabel, JPanel}

class WordMemoryPanel(controller: GameController, question: String, onNext: GameController => Unit)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    createSimpleQuestionAnswerGamePanel(
      "Try to remember these words",
      "Write all words:",
      controller,
      onNext,
      _.getQuestion,
      (ctrl, input) => ctrl.checkAnswer(input),
      Some(container => {
        container.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10))

        // TODO: Hide words after 30 s
        val words = question.split("\\s+")
        for word <- words do
          val lbl = new JLabel(word)
          lbl.setFont(pixelFont25)
          container.add(lbl)
      })
    )
