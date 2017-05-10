package game.physics

import scala.collection.mutable.Map

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.math.Vector2

class CharacterBody(world: World) {

  val parts = Map[String, BodyPart]()

  val joints = Map[String, RevoluteJoint]()

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

    joints(jointId) = world.createJoint(jointDef).asInstanceOf[RevoluteJoint]
  }

  def connect(jointId: String, partA: String, partB: Body, point: Vector2) : Unit = {
    val jointDef = new RevoluteJointDef()

    jointDef.initialize(parts(partA).body, partB, point)

    joints(jointId) = world.createJoint(jointDef).asInstanceOf[RevoluteJoint]
  }

  def getBodyPart(id: String) : BodyPart = {
    parts(id)
  }

  def removeJoint(id: String) : Unit = {
    val joint = joints(id)
    joints -= id

    world.destroyJoint(joint)
  }

  def removeBodyPart(id: String) : BodyPart = {
    val part = parts(id)
    parts -= id

    part
  }

  def setAngleTarget(jointId: String, angleTarget: Float, maxTorque: Float = 4.0f, gain: Float = 2.0f) : Unit = {
    var joint = joints(jointId)

    joint.enableMotor(true)
    joint.setMaxMotorTorque(maxTorque)

    val angleError = joint.getJointAngle() - angleTarget

    joint.setMotorSpeed(-gain * angleError)
  }

  def relaxJoint(jointId: String) : Unit = {
    var joint = joints(jointId)
    joint.enableMotor(false)
  }

  def setLimits(jointId: String, lowerLimit: Float, upperLimit: Float): Unit = {
    var joint = joints(jointId)

    joint.enableLimit(true)

    joint.setLimits(lowerLimit, upperLimit)
  }

  def activateMotor(jointId: String, speed: Float, torque: Float): Unit = {
    var joint = joints(jointId)

    joint.enableMotor(true)
    joint.setMaxMotorTorque(torque)

    joint.setMotorSpeed(speed)
  }
}
