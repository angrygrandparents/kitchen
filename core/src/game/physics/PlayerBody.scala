package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._


class PlayerBody(world: World, x: Float) {

  val torsoHeight = 2.0f;
  val legHeight = 1.0f;

  val torso = createTorso()
  // val head = createHead()
  val leftArmUpper = createLeftArmUpper()
  val leftArmLower = createLeftArmLower()

  val rightArmUpper = createRightArmUpper()
  val rightArmLower = createRightArmLower()

  val leftShoulderPivot = new Vector2(x - 0.5f, 2.4f)
  val leftShoulderJoint = createLeftShoulderJoint()

  val rightShoulderPivot = new Vector2(x + 0.5f, 2.4f)
  val rightShoulderJoint = createRightShoulderJoint()

  val leftLeg = createLeftLeg()
  val rightLeg = createRightLeg()

  attachParts()

  private def setBodyFixtureProperties(fixtureDef: FixtureDef) {
    fixtureDef.density = 0.4f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.PLAYER_MASK;
    fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK;
  }

  private def createTorso(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.allowSleep = false
    bodyDef.position.set(x, legHeight + torsoHeight / 2);
    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.8f, torsoHeight / 2)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  private def createLeftArmUpper(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.allowSleep = false

    bodyDef.position.set(x - 0.5f, legHeight + torsoHeight / 2);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createLeftArmLower(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.position.set(x - 0.5f, 1);
    bodyDef.allowSleep = false

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  private def createRightArmUpper(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.allowSleep = false

    bodyDef.position.set(x + 0.5f, legHeight + torsoHeight / 2);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createRightArmLower(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.position.set(x + 0.5f, 1);
    bodyDef.allowSleep = false

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createLeftLeg(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.position.set(x - 0.5f, legHeight / 2);

    bodyDef.`type` = BodyType.StaticBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, legHeight / 2)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createRightLeg(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.position.set(x + 0.5f, 0.5f);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    setBodyFixtureProperties(fixtureDef)

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createLeftShoulderJoint() : Joint = {
    val jointDef = new RevoluteJointDef()

    jointDef.initialize(torso, leftArmUpper, leftShoulderPivot)
    jointDef.enableMotor = true
    jointDef.motorSpeed = -1.0f
    jointDef.maxMotorTorque = 2.0f

    return world.createJoint(jointDef)
  }

  def createRightShoulderJoint() : Joint = {
    val jointDef = new RevoluteJointDef()

    jointDef.initialize(torso, rightArmUpper, rightShoulderPivot)
    jointDef.enableMotor = true
    jointDef.motorSpeed = 1.0f
    jointDef.maxMotorTorque = 2.0f

    return world.createJoint(jointDef)
  }

  def attachParts() {
    val elbowDef = new RevoluteJointDef()

    elbowDef.initialize(leftArmUpper, leftArmLower, new Vector2(x - 0.5f, 1.5f))
    elbowDef.enableLimit = true
    elbowDef.lowerAngle = -0.8f * Math.PI.toFloat
    elbowDef.upperAngle = 0.1f * Math.PI.toFloat

    val rightElbowDef = new RevoluteJointDef()

    rightElbowDef.initialize(rightArmUpper, rightArmLower, new Vector2(x + 0.5f, 1.5f))
    rightElbowDef.enableLimit = true
    rightElbowDef.lowerAngle = -0.8f * Math.PI.toFloat
    rightElbowDef.upperAngle = 0.1f * Math.PI.toFloat

    val pelvisJointLeftDef = new RevoluteJointDef()
    pelvisJointLeftDef.initialize(leftLeg, torso, new Vector2(x - 0.5f, 1.0f))
    pelvisJointLeftDef.lowerAngle = -0.3f * Math.PI.toFloat
    pelvisJointLeftDef.upperAngle = 0.3f * Math.PI.toFloat
    pelvisJointLeftDef.enableLimit = true

    val pelvisJointRightDef = new RevoluteJointDef()
    pelvisJointRightDef.initialize(rightLeg, torso, new Vector2(x + 0.5f, 1.0f))
    pelvisJointRightDef.lowerAngle = -0.3f * Math.PI.toFloat
    pelvisJointRightDef.upperAngle = 0.3f * Math.PI.toFloat
    pelvisJointRightDef.enableLimit = true

    world.createJoint(elbowDef)
    world.createJoint(rightElbowDef)
    world.createJoint(pelvisJointLeftDef)
    world.createJoint(pelvisJointRightDef)
  }

}
