package controllers

import models.*
import scala.util.Random

case class GameController( currentGame: Option[MiniGameLogic] = None,
                           lastQuestion: Option[String] = None,
                           difficulty: Int = 1,
                           rand: Random = new Random()):

  private val gameList = List(FastCalcLogic, CountWordsLogic)

  def chooseCurrentGame(gameMode: String): GameController =
    val game = gameMode match
      case "Fast Calc" => Some(FastCalcLogic)
      case "Count Words" => Some(CountWordsLogic)
      case "Right Directions" => None //TODO: modify with RightDirectionsLogic
      case _ => None
    this.copy(currentGame = game)

  def getQuestion: String =
    currentGame.get.generateQuestion(difficulty) //TODO: handle difficulty increase

  def checkAnswer(answer: String): Boolean =
    currentGame.get.validateAnswer(lastQuestion.get, answer.toInt)

  def selectRandomGame(): GameController =
    val selected = gameList(rand.between(0, gameList.length))
    this.copy(currentGame = Some(selected))

  def increaseDifficulty(): GameController =
    this.copy(difficulty = difficulty + 1)

