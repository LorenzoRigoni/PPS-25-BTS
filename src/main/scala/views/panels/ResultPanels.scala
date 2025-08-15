package views.panels

import controllers.GameController
import views.*
import utils.constants.GUIConstants.*
import scala.List

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

class ResultPanelsImpl extends ResultPanels:

  private def createBaseResultPanel(controller: GameController, titleText: String): JPanel =
    val borderInset = 50
    val panel       = new BackgroundImagePanel("src\\main\\resources\\AgeTestResultBackgroundImage.png")
    panel.setLayout(new BorderLayout())
    panel.setBorder(new EmptyBorder(borderInset, borderInset, borderInset, borderInset))
    val title       = new JLabel(titleText)
    title.setFont(PIXEL_FONT25)
    title.setHorizontalAlignment(SwingConstants.CENTER)
    val homeButton  =
      UIHelper.createStyledButton(
        "Home",
        Dimension(HOME_BUTTON_W, HOME_BUTTON_H),
        PIXEL_FONT15,
        Color.WHITE,
        CUSTOM_BLUE
      )
    homeButton.addActionListener(_ => {
      MenuView(controller).show()
      SwingUtilities.getWindowAncestor(panel).dispose()
    })
    val bottomPanel = new JPanel()
    bottomPanel.setOpaque(false)
    bottomPanel.add(homeButton)
    panel.add(title, BorderLayout.NORTH)
    panel.add(bottomPanel, BorderLayout.SOUTH)
    panel

  override def TestResultPanel(controller: GameController, age: Int): JPanel =
    val panel    = createBaseResultPanel(controller, "Your brain age is:")
    val ageLabel = new JLabel(age.toString)
    ageLabel.setFont(PIXEL_FONT70)
    ageLabel.setHorizontalAlignment(SwingConstants.CENTER)
    panel.add(ageLabel, BorderLayout.CENTER)
    panel

  override def GameResultPanel(
      controller: GameController,
      correctAnswers: Int,
      wrongAnswers: Int,
      time: Int
  ): JPanel =
    val gridRows        = 3
    val gridCols        = 1
    val gridHGap        = 10
    val gridVGap        = 20
    val iconSizeDivisor = 20
    val iconSize        = UIHelper.getResponsiveIconSize(iconSizeDivisor)
    val panel           = createBaseResultPanel(controller, "Your results:")
    val resultsPanel    = new JPanel()
    val rows            = List(
      ("greenCheck.png", s"Correct Answers: $correctAnswers"),
      ("redCross.png", s"Wrong Answers: $wrongAnswers"),
      ("clock.png", s"Time: $time seconds")
    )
    resultsPanel.setOpaque(false)
    resultsPanel.setLayout(new GridLayout(gridRows, gridCols, gridHGap, gridVGap))
    rows
      .map((icon, text) => UIHelper.createResultRow(icon, text, iconSize))
      .foreach(resultsPanel.add)
    panel.add(resultsPanel, BorderLayout.CENTER)
    panel
