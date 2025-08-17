package views.panels

import controllers.GameController
import utils.SimpleTextQuestion

import javax.swing.{JPanel, Timer}

/**
 * Class used to create the panel for the game "Word Memory"
 * @param controller
 *   the controller that manages the game logic and state
 * @param onNext
 *   callback invoked when the player completes the current question
 * @param question
 *   the question to display
 */
class WordMemoryPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: SimpleTextQuestion
) extends SimpleQuestionAnswerGamePanel[SimpleTextQuestion]:
  override def panel(): JPanel =
    val TIMER_WORD_MEMORY = 10000
    val (panel, _)        = createSimpleQuestionAnswerGamePanel(
      "Try to remember these words",
      question,
      "Write all words:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some(simpleLabelRenderer)
    )
    inputField.setEnabled(false)
    val timer             = new Timer(
      TIMER_WORD_MEMORY,
      _ =>
        questionPanel.removeAll()
        questionPanel.revalidate()
        questionPanel.repaint()
        inputField.setEnabled(true)
        inputField.requestFocusInWindow()
    )
    timer.setRepeats(false)
    timer.start()
    panel
