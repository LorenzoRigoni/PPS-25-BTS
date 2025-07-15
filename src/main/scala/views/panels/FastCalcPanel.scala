package views.panels

import javax.swing.*
import java.awt.*

object FastCalcPanel extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val correctAnswer = "38" //TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = "11 + (3 x 9) = ",
      textInputLabel = "Your result: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )
