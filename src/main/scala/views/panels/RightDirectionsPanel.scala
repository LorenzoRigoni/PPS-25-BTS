package views.panels

import controllers.GameController

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Right Directions.
 */
class RightDirectionsPanel(controller: GameController, onNext: GameController => Unit)
    extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    createSimpleQuestionAnswerGamePanel(
      question = controller.lastQuestion.get,
      textInputLabel = "Your answer: ",
      validate = input =>
        if controller.checkAnswer(input) then
          val increased = controller.increaseDifficulty()
          onNext(increased)
          ("Correct!", Color.GREEN)
        else (s"Wrong!", Color.RED),
      controller = controller
    )
