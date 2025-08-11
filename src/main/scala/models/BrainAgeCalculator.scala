package models

import utils.QuestionResult
import utils.BrainAgeConstants.{BASE_AGE, ERROR_PERCENT, MAXIMUM_AGE, SECONDS_UNITY}

/**
 * This object is a helper for calculate the brain age of the player.
 */
object BrainAgeCalculator:

  /**
   * Calculate the brain age of the player. The result is an integer based on the time used to
   * answer and the number of errors.
   * @param results
   *   The stats of the game played by the user
   * @return
   *   An integer that represents the brain age of the player
   */
  def calcBrainAge(results: List[QuestionResult]): Int =
    if results.isEmpty then return BASE_AGE
    val avgTime      = results.map(_.responseTime).sum.toDouble / results.length
    val errorRate    = results.count(!_.isCorrect).toDouble / results.length
    val timePenalty  = (avgTime / SECONDS_UNITY).toInt
    val errorPenalty = (errorRate * ERROR_PERCENT).toInt
    val result       = BASE_AGE + timePenalty + errorPenalty
    if result <= MAXIMUM_AGE then result else MAXIMUM_AGE
