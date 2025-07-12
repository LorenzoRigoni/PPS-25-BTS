package views

import javax.swing.*
import java.awt.*
import java.util.{Timer, TimerTask}
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

object MainView:

  def show(gamePanels: GamePanels): Unit =
    val frame = new JFrame("Let's play!")
    frame.setSize(500, 400)

    val mainPanel = new JPanel(new BorderLayout())

    val timeLabel = new JLabel("Time left: 120 seconds", SwingConstants.CENTER)
    timeLabel.setFont(new Font("Arial", Font.BOLD, 18))
    mainPanel.add(timeLabel, BorderLayout.NORTH)

    val centerPanel = new JPanel(new BorderLayout())
    mainPanel.add(centerPanel, BorderLayout.CENTER)

    val timeLeft = new AtomicInteger(120)
    val currentTimer = new AtomicReference[Timer]()

    def startTimer(): Unit =
      Option(currentTimer.getAndSet(new Timer())).foreach(_.cancel())
      timeLeft.set(120)
      timeLabel.setText("Time left: 120 seconds")

      val timer = new Timer()
      currentTimer.set(timer)

      val task = new TimerTask {
        override def run(): Unit = {
          val t = timeLeft.decrementAndGet()
          SwingUtilities.invokeLater(() => timeLabel.setText(s"Time left: ${t} seconds"))
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

    val buttonPanel = new JPanel(new FlowLayout())
    val buttons = Seq(
      new JButton("Fast Calc") -> gamePanels.fastCalcPanel _,
      new JButton("Count Words") -> gamePanels.countWordsPanel _,
      new JButton("Right Direction") -> gamePanels.rightDirectionsPanel _
    )

    buttons.foreach((button, panelSupplier) => {
      button.addActionListener(_ => {
        centerPanel.removeAll()
        centerPanel.add(panelSupplier(), BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
        startTimer()
      })
      buttonPanel.add(button)
    })

    mainPanel.add(buttonPanel, BorderLayout.SOUTH)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)

    startTimer()