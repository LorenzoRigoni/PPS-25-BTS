package utils

import utils.enums.ColoredCountColors

sealed trait Question

case class SimpleTextQuestion(text: String) extends Question

case class ColoredCountQuestion(
    numbersWithColor: Seq[(Int, ColoredCountColors)],
    colorRequired: ColoredCountColors
) extends Question
