package models

import utils.constants.BrainAgeCalculatorConstants.{BASE_AGE, MAXIMUM_AGE}

/**
 * This object is a helper for calculate the brain age of the player.
 */
object BrainAgeCalculator:
  private val SECONDS_UNITY = 1000
  private val ERROR_PERCENT = 50

  /**
   * Calculate the brain age of the player. The result is an integer based on the time used to
   * answer and the number of errors.
   * @param results
   *   The stats of the game played by the user
   * @return
   *   An integer that represents the brain age of the player
   */
  def calcBrainAge(results: List[QuestionResult]): Int =
    if results.isEmpty then return MAXIMUM_AGE
    val avgTime      = results.map(_.responseTime).sum.toDouble / results.length
    val errorRate    = results.count(!_.isCorrect).toDouble / results.length
    val timePenalty  = (avgTime / SECONDS_UNITY).toInt
    val errorPenalty = (errorRate * ERROR_PERCENT).toInt
    val result       = BASE_AGE + timePenalty + errorPenalty
    if result <= MAXIMUM_AGE then result else MAXIMUM_AGE
