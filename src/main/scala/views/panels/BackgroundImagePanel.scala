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
    val stream = getClass.getResourceAsStream(imagePath)
    if stream == null then
      throw new RuntimeException(s"Resource not found: $imagePath")
    try ImageIO.read(stream)
    finally stream.close()

  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    g.drawImage(image, 0, 0, getWidth, getHeight, this)
