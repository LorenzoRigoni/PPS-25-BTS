package views.panels

import controllers.GameController
import views.*
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * This trait represents the possible result panels.
 */
sealed trait ResultPanels:
  /**
   * Creates the panel that shows the age calculated during the Age Test.
   * @param controller
   * @param age calculated
   * @return
   */
  def TestResultPanel(controller: GameController, age: Int): JPanel

  /**
   * Creates the panel that shows the results of the minigame just completed.
   * @param controller
   * @param correctAnswers number of correct answers
   * @param wrongAnswers number of wrong answers
   * @param time needed to complete the minigame
   * @return
   */
  def GameResultPanel(controller: GameController, correctAnswers: Int, wrongAnswers: Int, time: Int): JPanel

class ResultPanelsImpl extends ResultPanels, BaseView:

  override def TestResultPanel(controller: GameController, age: Int): JPanel =
    val panel = new JPanel()
    panel.setLayout(new BorderLayout())
    panel.setBackground(whiteColor)
    panel.setBorder(new EmptyBorder(50, 50, 50, 50))

    val title = new JLabel("Your brain age is:")
    title.setFont(pixelFont25)
    title.setHorizontalAlignment(SwingConstants.CENTER)

    val ageLabel = new JLabel(age.toString)
    ageLabel.setFont(pixelFont70)
    ageLabel.setHorizontalAlignment(SwingConstants.CENTER)

    val homeButton = createStyledButton("Home", Dimension(200, 40), pixelFont15, customBlueColor, whiteColor)
    homeButton.addActionListener(_ => MenuView(controller).show())

    val bottomPanel = new JPanel()
    bottomPanel.setBackground(whiteColor)
    bottomPanel.add(homeButton)

    panel.add(title, BorderLayout.NORTH)
    panel.add(ageLabel, BorderLayout.CENTER)
    panel.add(bottomPanel, BorderLayout.SOUTH)

    panel

  override def GameResultPanel(controller: GameController, correctAnswers: Int, wrongAnswers: Int, time: Int): JPanel = ???