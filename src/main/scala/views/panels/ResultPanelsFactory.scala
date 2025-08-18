package views.panels

import controllers.GameController
import views.*
import utils.constants.GUIConstants.*
import scala.List

import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

/**
 * This is a factory trait to create panels to show results.
 */
sealed trait ResultPanelsFactory:
  /**
   * Creates the panel that shows the age calculated during the Age Test.
   * @param controller
   *   the controller that manages the game logic and state
   * @param age
   *   calculated
   * @return
   */
  def testResultPanel(controller: GameController, age: Int): JPanel

  /**
   * Creates the panel that shows the results of the minigame just completed.
   * @param controller
   *   the controller that manages the game logic and state
   * @param correctAnswers
   *   number of correct answers
   * @param wrongAnswers
   *   number of wrong answers
   * @param time
   *   needed to complete the minigame
   * @return
   */
  def gameResultPanel(
      controller: GameController,
      correctAnswers: Int,
      wrongAnswers: Int,
      time: Int
  ): JPanel

class ResultPanelsFactoryImpl extends ResultPanelsFactory:
  private val BORDER_INSET                                                                 = 50
  private val GRID_ROWS                                                                    = 3
  private val GRID_COLS                                                                    = 1
  private val GRID_H_GAP                                                                   = 10
  private val GRID_V_GAP                                                                   = 20
  private val ICON_SIZE_DIVISOR                                                            = 20
  private def createBaseResultPanel(controller: GameController, titleText: String): JPanel =
    val panel       = new BackgroundImagePanel("src\\main\\resources\\AgeTestResultBackgroundImage.png")
    panel.setLayout(new BorderLayout())
    panel.setBorder(new EmptyBorder(BORDER_INSET, BORDER_INSET, BORDER_INSET, BORDER_INSET))
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
      MenuView(controller).show
      SwingUtilities.getWindowAncestor(panel).dispose()
    })
    val bottomPanel = new JPanel()
    bottomPanel.setOpaque(false)
    bottomPanel.add(homeButton)
    panel.add(title, BorderLayout.NORTH)
    panel.add(bottomPanel, BorderLayout.SOUTH)
    panel

  override def testResultPanel(controller: GameController, age: Int): JPanel =
    val panel    = createBaseResultPanel(controller, "Your brain age is:")
    val ageLabel = new JLabel(age.toString)
    ageLabel.setFont(PIXEL_FONT70)
    ageLabel.setHorizontalAlignment(SwingConstants.CENTER)
    panel.add(ageLabel, BorderLayout.CENTER)
    panel

  override def gameResultPanel(
      controller: GameController,
      correctAnswers: Int,
      wrongAnswers: Int,
      time: Int
  ): JPanel =
    val iconSize     = UIHelper.getResponsiveIconSize(ICON_SIZE_DIVISOR)
    val panel        = createBaseResultPanel(controller, "Your results:")
    val resultsPanel = new JPanel()
    val rows         = List(
      ("greenCheck.png", s"Correct Answers: $correctAnswers"),
      ("redCross.png", s"Wrong Answers: $wrongAnswers"),
      ("clock.png", s"Time: $time seconds")
    )
    resultsPanel.setOpaque(false)
    resultsPanel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS, GRID_H_GAP, GRID_V_GAP))
    rows
      .map((icon, text) => UIHelper.createResultRow(icon, text, iconSize))
      .foreach(resultsPanel.add)
    panel.add(resultsPanel, BorderLayout.CENTER)
    panel
