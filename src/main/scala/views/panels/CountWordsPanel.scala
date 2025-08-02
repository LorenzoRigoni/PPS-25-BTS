package views.panels

import controllers.GameController

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Count Words.
 */
class CountWordsPanel(controller: GameController, question: String, onNext: GameController => Unit)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    createSimpleQuestionAnswerGamePanel(
      question,
      "Number of words:",
      controller,
      onNext,
      _.getQuestion,
      (ctrl, input) => ctrl.checkAnswer(input)
    )
