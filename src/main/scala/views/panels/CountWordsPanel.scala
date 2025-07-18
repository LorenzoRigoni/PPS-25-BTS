package views.panels

import controllers.GameController

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Count Words.
 */
class CountWordsPanel(controller: GameController, question: String) extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    createSimpleQuestionAnswerGamePanel(
      question = question,
      textInputLabel = "Number of words: ",
      validate = input =>
        if controller.checkAnswer(input) then ("Correct!", Color.GREEN)
        else (s"Wrong!", Color.RED),
      controller = controller
    )
