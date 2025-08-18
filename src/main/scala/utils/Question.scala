package utils

import utils.enums.ColoredCountColors

/**
 * This trait represents the type of question. It is used for add constraints on the type of generic
 * for the question.
 */
sealed trait Question

/**
 * The simple type of question wrapped in a string.
 *
 * @param text the text of the question
 */
case class SimpleTextQuestion(text: String) extends Question

/**
 * The type of question for the mini-game "Colored Count".
 *
 * @param numbersWithColor A zipped sequence with (number, color)
 * @param colorRequired The color required
 */
case class ColoredCountQuestion(
    numbersWithColor: Seq[(Int, ColoredCountColors)],
    colorRequired: ColoredCountColors
) extends Question
