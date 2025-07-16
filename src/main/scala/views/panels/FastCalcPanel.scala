package views.panels

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Fast Calc.
 */
object FastCalcPanel extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val correctAnswer = "38" // TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = "11 + (3 x 9) = ",
      textInputLabel = "Your result: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )
