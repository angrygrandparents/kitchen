package game.physics

object CollisionCategory {
  val GRANDMA : Short = 0x0004
  val GRANDPA : Short = 0x0008
  val PLAYER_MASK : Short = 0x0002
  val STATIC_MASK : Short = 0x0001
  val CONTROL_MASK : Short = 0x1000
}
