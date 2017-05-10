package game.physics

import scala.collection.mutable.Map

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.math.Vector2

class CharacterBody(world: World) {

  val parts = Map[String, BodyPart]()

  val joints = Map[String, Joint]()

  def addBodyPart(id: String, part: BodyPart) {
    parts(id) = part
  }

  def render(batch: SpriteBatch) {
    parts.values.toVector.sortBy(_.drawOrder).foreach {
      _.render(batch)
    }
  }

  def translate(amount: Vector2) {
    parts.foreach { case (id, part) =>
      part.translate(amount)
    }
  }

  def connect(jointId: String, partA: String, partB: String, point: Vector2): Unit = {
    val jointDef = new RevoluteJointDef()

    jointDef.initialize(parts(partA).body, parts(partB).body, point)
    jointDef.enableMotor = true
    // jointDef.motorSpeed = -1.0f
    jointDef.maxMotorTorque = 2.0f

    joints(jointId) = world.createJoint(jointDef)
  }
}
