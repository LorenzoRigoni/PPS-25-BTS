package models

import utils.QuestionResult

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
    val baseAge      = 20
    val maximumAge   = 100
    if results.isEmpty then return baseAge
    val avgTime      = results.map(_.responseTime).sum.toDouble / results.length
    val errorRate    = results.count(!_.isCorrect).toDouble / results.length
    val timePenalty  = (avgTime / 1000).toInt
    val errorPenalty = (errorRate * 50).toInt
    val result       = baseAge + timePenalty + errorPenalty
    if result <= maximumAge then result else maximumAge
