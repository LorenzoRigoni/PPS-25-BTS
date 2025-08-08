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
   * @param age
   *   calculated
   * @return
   */
  def TestResultPanel(controller: GameController, age: Int): JPanel

  /**
   * Creates the panel that shows the results of the minigame just completed.
   * @param controller
   * @param correctAnswers
   *   number of correct answers
   * @param wrongAnswers
   *   number of wrong answers
   * @param time
   *   needed to complete the minigame
   * @return
   */
  def GameResultPanel(
      controller: GameController,
      correctAnswers: Int,
      wrongAnswers: Int,
      time: Int
  ): JPanel

class ResultPanelsImpl extends ResultPanels, BaseView:
  private def createBaseResultPanel(controller: GameController, titleText: String): JPanel =
    val panel       = new BackgroundImagePanel("src\\main\\resources\\AgeTestResultBackgroundImage.png")
    panel.setLayout(new BorderLayout())
    // panel.setBackground(whiteColor)
    panel.setBorder(new EmptyBorder(50, 50, 50, 50))
    val title       = new JLabel(titleText)
    title.setFont(pixelFont25)
    title.setHorizontalAlignment(SwingConstants.CENTER)
    val homeButton  =
      createStyledButton("Home", Dimension(200, 40), pixelFont15, whiteColor, customBlueColor)
    homeButton.addActionListener(_ => {
      MenuView(controller).show()
      SwingUtilities.getWindowAncestor(panel).dispose()
    })
    val bottomPanel = new JPanel()
    // bottomPanel.setBackground(whiteColor)
    bottomPanel.setOpaque(false)
    bottomPanel.add(homeButton)
    panel.add(title, BorderLayout.NORTH)
    panel.add(bottomPanel, BorderLayout.SOUTH)
    panel

  override def TestResultPanel(controller: GameController, age: Int): JPanel =
    val panel    = createBaseResultPanel(controller, "Your brain age is:")
    val ageLabel = new JLabel(age.toString)
    ageLabel.setFont(pixelFont70)
    ageLabel.setHorizontalAlignment(SwingConstants.CENTER)
    panel.add(ageLabel, BorderLayout.CENTER)
    panel

  override def GameResultPanel(
      controller: GameController,
      correctAnswers: Int,
      wrongAnswers: Int,
      time: Int
  ): JPanel =
    val iconSize = getResponsiveIconSize(20)
    val panel    = createBaseResultPanel(controller, "Your results:")

    def loadIcon(name: String, size: Int): ImageIcon =
      val url     = getClass.getResource(s"/" + name)
      val image   = new ImageIcon(url).getImage
      val resized = image.getScaledInstance(size, size, Image.SCALE_SMOOTH)
      new ImageIcon(resized)

    def resultRow(iconName: String, text: String): JPanel =
      val rowPanel  = new JPanel(new FlowLayout(FlowLayout.CENTER))
      // rowPanel.setBackground()
      rowPanel.setOpaque(false)
      val iconLabel = new JLabel(loadIcon(iconName, iconSize))
      val textLabel = new JLabel(text)
      textLabel.setFont(pixelFont15)
      rowPanel.add(iconLabel)
      rowPanel.add(Box.createRigidArea(new Dimension(10, 0)))
      rowPanel.add(textLabel)
      rowPanel

    val resultsPanel = new JPanel()
    // resultsPanel.setBackground(whiteColor)
    resultsPanel.setOpaque(false)
    resultsPanel.setLayout(new GridLayout(3, 1, 10, 20))
    resultsPanel.add(resultRow("greenCheck.png", s"Correct Answers: $correctAnswers"))
    resultsPanel.add(resultRow("redCross.png", s"Wrong Answers: $wrongAnswers"))
    resultsPanel.add(resultRow("clock.png", s"Time: $time seconds"))
    panel.add(resultsPanel, BorderLayout.CENTER)
    panel
