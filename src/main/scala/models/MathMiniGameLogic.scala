package models

/**
 * This trait represents the implementation of logics for the math mini-games.
 *
 * @tparam Q
 *   The type of question
 */
trait MathMiniGameLogic[Q <: Question]:
  self: MiniGameLogic[Q, Int, Boolean] =>

  val lastQuestion: Option[Q]

  /**
   * Set the difficulty step.
   *
   * @return
   *   difficulty step
   */
  protected def difficultyStep: Int

  /**
   * Generate the correct answer for the question.
   *
   * @param question
   *   The question
   * @return
   *   the correct answer
   */
  protected def correctAnswer(question: Q): Int

  /**
   * Generate the copy of the logic for the new question.
   *
   * @param question
   *   The question generated
   * @return
   *   a copy of the logic
   */
  protected def withNewQuestion(question: Q): MiniGameLogic[Q, Int, Boolean]

  /**
   * Advance with a new question
   * @param question
   *   The question generated
   * @return
   *   a copy of the logic and the question
   */
  protected def advance(question: Q): (MiniGameLogic[Q, Int, Boolean], Q) =
    (
      withNewQuestion(question),
      question
    )

  override def parseAnswer(answer: String): Option[Int] = answer.trim.toIntOption

  override def validateAnswer(answer: Int): Boolean = lastQuestion match
    case Some(q) => answer == correctAnswer(q)
    case _       => false
