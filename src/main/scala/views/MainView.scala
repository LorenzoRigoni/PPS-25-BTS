package views

import views.BrainTraining.{createStyledButton, customBlueColor, pixelFontSmall, whiteColor}

import javax.swing.*
import java.awt.*
import java.util.{Timer, TimerTask}
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

//TODO: remove buttons here
object MainView extends BaseView:

  def show(gamePanels: GamePanels): Unit =
    val frame = new JFrame("Let's play!")
    val frameWidth = 500
    val frameHeight = 600
    val buttonDimension = new Dimension(300,50)
    val timeLeft = new AtomicInteger(120)
    val currentTimer = new AtomicReference[Timer]()
    val mainPanel = new JPanel(new BorderLayout())
    val buttonPanel = new JPanel(new FlowLayout())

    frame.setSize(frameWidth, frameHeight)
    frame.setBackground(whiteColor)
    centerFrame(frame, frameWidth, frameHeight)

    val timeLabel = new JLabel("Time left: 120 seconds", SwingConstants.CENTER)
    timeLabel.setFont(pixelFontSmall)
    mainPanel.add(timeLabel, BorderLayout.NORTH)

    val centerPanel = new JPanel(new BorderLayout())
    mainPanel.add(centerPanel, BorderLayout.CENTER)

    def startTimer(): Unit =
      Option(currentTimer.getAndSet(new Timer())).foreach(_.cancel())
      timeLeft.set(120)
      timeLabel.setText("Time left: 120 seconds")

      val timer = new Timer()
      currentTimer.set(timer)

      val task = new TimerTask {
        override def run(): Unit = {
          val t = timeLeft.decrementAndGet()
          SwingUtilities.invokeLater(() => timeLabel.setText(s"Time left: $t seconds"))
          if (t <= 0) {
            timer.cancel()
            currentTimer.set(new Timer())
            SwingUtilities.invokeLater(() => {
              JOptionPane.showMessageDialog(frame, "Time's up!")
              frame.dispose()
            })
          }
        }
      }
      timer.scheduleAtFixedRate(task, 1000, 1000)

    def attachGameButton(
                          button: JButton,
                          centerPanel: JPanel,
                          panelSupplier: () => JPanel,
                          rules: String,
                          startTimer: () => Unit): Unit =
      button.addActionListener(_ => {
        JOptionPane.showMessageDialog(centerPanel, rules, "Rules of the mini game", JOptionPane.INFORMATION_MESSAGE)
        centerPanel.removeAll()
        mainPanel.remove(buttonPanel)
        centerPanel.add(panelSupplier(), BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
        startTimer()
      })


    val buttons = Seq(
      createStyledButton("Fast Calc", buttonDimension, pixelFontSmall, customBlueColor, whiteColor) -> (gamePanels.fastCalcPanel _, "Enter the result of the operation by press the 'Enter' button"),
      createStyledButton("Count Words", buttonDimension, pixelFontSmall, customBlueColor, whiteColor) -> (gamePanels.countWordsPanel _, "Enter the number of words of the sentence by press the 'Enter' button"),
      createStyledButton("Right Directions", buttonDimension, pixelFontSmall, customBlueColor, whiteColor) -> (gamePanels.rightDirectionsPanel _, "Enter the right directions suggested by press the arrow buttons")
    )

    buttons.foreach {
      case (button, (panelSupplier, rules)) =>
        attachGameButton(button, centerPanel, panelSupplier, rules, startTimer)
        button.setAlignmentX(Component.CENTER_ALIGNMENT)
        buttonPanel.add(Box.createVerticalStrut(40))
        buttonPanel.add(button)
    }

    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS))
    mainPanel.add(buttonPanel, BorderLayout.SOUTH)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)

    startTimer()