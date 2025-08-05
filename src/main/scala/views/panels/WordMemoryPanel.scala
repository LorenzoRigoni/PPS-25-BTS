package views.panels

import controllers.GameController

import java.awt.FlowLayout
import javax.swing.{JLabel, JPanel}

class WordMemoryPanel(controller: GameController, question: String, onNext: GameController => Unit)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Try to remember these words",
      "Write all words:",
      controller,
      nextController => {
        val nextWithResults = nextController.nextGame
        onNext(nextWithResults)
      },
      _.getNewQuestion,
      (ctrl, input) =>
        println(s"Risultati prima: ${ctrl.results}")
        val (newCtrl, isCorrect) = ctrl.checkAnswer(input)
        println(s"Risultati dopo: ${newCtrl.results}")
        (newCtrl, isCorrect)
      ,
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
    panel
