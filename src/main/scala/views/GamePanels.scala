package views

import javax.swing.*
import java.awt.*

trait GamePanels:
  def fastCalcPanel(): JPanel
  def countWordsPanel(): JPanel
  def rightDirections(): JPanel

object GamePanels extends GamePanels:
  override def fastCalcPanel(): JPanel =
    val panel = new JPanel()
    panel.add(new JLabel("Fast Calc panel"))
    //TODO: implement Fast Calc panel here
    panel

  override def countWordsPanel(): JPanel =
    val panel = new JPanel()
    panel.add(new JLabel("Count Words panel"))
    //TODO: implement Count Words panel here
    panel

  override def rightDirections(): JPanel =
    val panel = new JPanel()
    panel.add(new JLabel("Right Directions panel"))
    //TODO: implement Right Directions panel here
    panel