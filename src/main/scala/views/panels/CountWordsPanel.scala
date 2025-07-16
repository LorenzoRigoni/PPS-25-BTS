package views.panels

import javax.swing.*
import java.awt.*

/**
 * This objects represents the view of the mini-game Count Words.
 */
object CountWordsPanel extends SimpleQuestionAnswerGamePanel:
  def panel(): JPanel =
    val sentence      = "This is a simple sentence"
    val correctAnswer =
      sentence.split("\\s+").count(_.nonEmpty).toString // TODO: use game model (still to implement)
    createSimpleQuestionAnswerGamePanel(
      question = sentence,
      textInputLabel = "Number of words: ",
      validate = input =>
        if input == correctAnswer then ("Correct!", Color.GREEN)
        else (s"Wrong! The result was $correctAnswer", Color.RED)
    )
