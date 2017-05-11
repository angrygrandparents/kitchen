package game.physics

object CollisionData {
  private var _nextId = 1L
  def nextId() = {
    val temp = _nextId
    _nextId += 1
    temp
  }
}

case class CollisionData(isItem: Boolean, isGrandma: Boolean = false, isCritical: Boolean = false) {
  val id = CollisionData.nextId()
}
