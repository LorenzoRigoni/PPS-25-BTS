package models

/**
 * This case class contains the response of the user to a question.
 *
 * @param responseTime
 *   The time used to answer
 * @param isCorrect
 *   The correctness of the answer
 */
case class QuestionResult(responseTime: Long, isCorrect: Boolean)
