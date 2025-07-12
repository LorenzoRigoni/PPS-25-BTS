package views

import javax.swing.*
import java.awt.*

object BrainTraining:

  def show(gamePanels: GamePanels): Unit =
    val frame = new JFrame("Brain Testing")
    frame.setSize(500, 400)

    val mainPanel = new JPanel(new BorderLayout())

    val centerPanel = new JPanel(new BorderLayout())
    mainPanel.add(centerPanel, BorderLayout.CENTER)

    val buttonPanel = new JPanel(new FlowLayout())
    val buttons = Seq(
      new JButton("Fast Calc") -> gamePanels.fastCalcPanel _,
      new JButton("Count Words") -> gamePanels.countWordsPanel _,
      new JButton("Right Directions") -> gamePanels.rightDirectionsPanel _
    )

    buttons.foreach((button, panelSupplier) => {
      button.addActionListener(_ => {
        centerPanel.removeAll()
        centerPanel.add(panelSupplier(), BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
      })
      buttonPanel.add(button)
    })

    mainPanel.add(buttonPanel, BorderLayout.NORTH)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)