package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._


class Player(world: World) {

  val body = new PlayerBody(world, -4)



}
