import views.MenuView

import javax.swing.SwingUtilities

object Main extends App:
  SwingUtilities.invokeLater(() => MenuView.show())