package views.panels

import controllers.GameController
import utils.SimpleTextQuestion

import javax.swing.*
import java.awt.*

/**
 * Class used to create the panel for the game "Count Words"
 * @param controller
 *   the controller that manages the game logic and state
 * @param onNext
 *   callback invoked when the player completes the current question
 * @param question
 *   the question to display
 */
class CountWordsPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: SimpleTextQuestion
) extends SimpleQuestionAnswerGamePanel[SimpleTextQuestion]:
  override def panel(): JPanel =
    val (panel, _) = createSimpleQuestionAnswerGamePanel(
      "Count the words",
      question,
      "Number of words:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some(simpleLabelRenderer)
    )
    panel
