package views.panels

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Right Directions.
 */
object RightDirectionsPanel extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val correctAnswer = "" // TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = "NOT RIGHT AND NOT NOT LEFT",
      textInputLabel = "Your answer: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )
