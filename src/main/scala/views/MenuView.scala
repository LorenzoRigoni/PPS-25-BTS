package views

import javax.swing.*
import java.awt.*

object MenuView:
  private val frame = new JFrame("Menù")

  def show(): Unit =
    frame.setSize(300, 150)

    val panel = new JPanel()
    panel.setLayout(new GridLayout(2, 1, 0, 0))

    val brainAgingButton = new JButton("Brain Aging")
    val brainTrainingButton = new JButton("Brain Training")

    brainAgingButton.addActionListener(_ => {
      frame.dispose()
      MainView.show(GamePanels)
    })

    brainTrainingButton.addActionListener(_ =>
      JOptionPane.showMessageDialog(frame, "Brain Testing not implemented.")
    )

    panel.add(brainAgingButton)
    panel.add(brainTrainingButton)

    frame.getContentPane.add(panel)
    frame.setVisible(true)