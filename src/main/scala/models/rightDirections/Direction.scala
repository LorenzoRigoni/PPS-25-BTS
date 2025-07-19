package models.rightDirections

enum Direction:
  private case Up, Down, Left, Right

object Direction:
  def all: List[Direction] = List(Up, Down, Left, Right)

  def fromString(s: String): Option[Direction] = s.toLowerCase match
    case "up" => Some(Up)
    case "down" => Some(Down)
    case "left"  => Some(Left)
    case "right"  => Some(Right)