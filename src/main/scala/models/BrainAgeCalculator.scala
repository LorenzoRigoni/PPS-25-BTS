package models

import controllers.GameStats

/**
 * This object is a helper for calculate the brain age of the player.
 */
object BrainAgeCalculator:

  /**
   * Calculate the brain age of the player. The result is an integer based on the time used to answer
   * and the number of errors.
   * @param gameStats The stats of the game played by the user
   * @return An integer that represents the brain age of the player
   */
  def calcBrainAge(gameStats: GameStats): Int =
    val baseAge = 20
    val maximumAge = 100
    if gameStats.results.isEmpty then return baseAge
    val avgTime = gameStats.results.map(_.responseTime).sum.toDouble / gameStats.results.length
    val errorRate = gameStats.results.count(!_.isCorrect).toDouble / gameStats.results.length
    val timePenalty = (avgTime / 1000).toInt
    val errorPenalty = (errorRate * 50).toInt
    val result = baseAge + timePenalty + errorPenalty
    if result <= maximumAge then result else maximumAge
