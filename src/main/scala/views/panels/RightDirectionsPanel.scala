package views.panels

import controllers.GameController

import javax.swing.*
import java.awt.*
import java.awt.event.{KeyAdapter, KeyEvent}

class RightDirectionsPanel(controller: GameController, question: String, onNext: GameController => Unit)
    extends SimpleQuestionAnswerGamePanel:

  def panel(): JPanel =
    val panel = createSimpleQuestionAnswerGamePanel(
      question,
      "Your answer:",
      controller,
      onNext,
      _.getQuestion,
      (ctrl, input) => ctrl.checkAnswer(input)
    )

    panel.addKeyListener(new KeyAdapter {
      override def keyPressed(e: KeyEvent): Unit = {
        val answerOpt: Option[String] = e.getKeyCode match
          case KeyEvent.VK_W => Some("up")
          case KeyEvent.VK_A => Some("left")
          case KeyEvent.VK_S => Some("down")
          case KeyEvent.VK_D => Some("right")
          case KeyEvent.VK_J => Some("")
          case _             => None

        /*answerOpt.foreach { answer =>
          val (message, color) = validateAnswer(answer)
        }*/
      }
    })

    panel.setFocusable(true)
    SwingUtilities.invokeLater(() => panel.requestFocusInWindow())

    panel
