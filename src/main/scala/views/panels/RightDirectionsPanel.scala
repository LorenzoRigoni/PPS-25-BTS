package views.panels

import controllers.GameController

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

class RightDirectionsPanel(
    controller: GameController,
    onNext: GameController => Unit,
    question: String
) extends SimpleQuestionAnswerGamePanel:

  def panel(): JPanel =
    val (panel, externalSubmit) = createSimpleQuestionAnswerGamePanel(
      "Follow directions",
      question,
      "Your answer:",
      controller,
      onNext,
      (ctrl, input) => ctrl.checkAnswer(input),
      Some(simpleLabelRenderer)
    )

    // Handle keyboard input
    panel.setFocusable(true)
    panel.requestFocusInWindow()

    val actionMap = panel.getActionMap
    val inputMap  = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)

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
