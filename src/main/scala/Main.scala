import controllers.GameController
import views.MenuView

import javax.swing.SwingUtilities

/**
 * Brain Training Scala - BTS
 *
 * Project of PPS course A.A. 2024/2025
 *
 * @author
 *   Moretti Riccardo
 * @author
 *   Rigoni Lorenzo
 * @author
 *   Versari Alessandra
 */
object Main extends App:
  SwingUtilities.invokeLater(() => MenuView.apply(GameController()).show())
