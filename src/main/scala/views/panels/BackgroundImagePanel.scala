package views.panels

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel

/**
 * This class is a helper for set the background image from resources.
 *
 * @param imagePath
 *   the path of the background image
 */
class BackgroundImagePanel(imagePath: String) extends JPanel:
  private val image: BufferedImage =
    val file = new File(imagePath)
    if !file.exists() then throw new RuntimeException(s"File not found: ${file.getAbsolutePath}")
    ImageIO.read(file)

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    g.drawImage(image, 0, 0, getWidth, getHeight, this)
