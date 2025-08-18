package views.panels

import controllers.GameController
import utils.SimpleTextQuestion

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

/**
 * Class used to create the panel for the game "Right Directions"
 * @param controller
 *   the controller that manages the game logic and state
 * @param onNext
 *   callback invoked when the player completes the current question
 * @param question
 *   the question to display
 */
class RightDirectionsPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: SimpleTextQuestion
) extends SimpleQuestionAnswerGamePanel[SimpleTextQuestion]:
  override def panel(): JPanel =
    val (panel, externalSubmit) = createSimpleQuestionAnswerGamePanel(
      "Follow directions",
      question,
      "Your answer:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input).get,
      Some(simpleLabelRenderer)
    )
    panel.setFocusable(true)
    panel.requestFocusInWindow()
    val actionMap               = panel.getActionMap
    val inputMap                = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)

    /**
     * @param key
     *   The key that, when pressed, will trigger the callback function
     * @param direction
     *   the direction that, as text, will be passed to the callback function
     */
    def bindKey(key: String, direction: String): Unit =
      inputMap.put(KeyStroke.getKeyStroke(key), key)
      actionMap.put(
        key,
        new AbstractAction {
          override def actionPerformed(e: ActionEvent): Unit =
            inputField.setText(direction)
            externalSubmit(direction)
        }
      )
    bindKey("W", "up")
    bindKey("UP", "up")
    bindKey("A", "left")
    bindKey("LEFT", "left")
    bindKey("S", "down")
    bindKey("DOWN", "down")
    bindKey("D", "right")
    bindKey("RIGHT", "right")
    bindKey("N", "")
    panel
