package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._


class PlayerBody(world: World, x: Float) {

  val torso = createTorso()
  val head = createHead()
  val leftArmUpper = createLeftArmUpper()
  val leftArmLower = createLeftArmLower()
  val leftLeg = createLeftLeg()

  val armControl = createArmControl()

  attachParts()

  def createTorso(): Body = {
    println("Make torso")
    val bodyDef = new BodyDef()

    bodyDef.position.set(x, 2);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.4f, 1.0f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 0.4f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.PLAYER_MASK;
    fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK;

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createHead(): Body = {
    val bodyDef = new BodyDef()

    bodyDef.position.set(x, 3.5f);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new CircleShape()
    shape.setRadius(0.4f);

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 0.4f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.PLAYER_MASK;
    fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK;

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createArmControl(): Body = {
    val bodyDef = new BodyDef()

    bodyDef.position.set(x, 0);

    bodyDef.`type` = BodyType.StaticBody

    val body = world.createBody(bodyDef)

    val shape = new CircleShape()
    shape.setRadius(0.1f);

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 0.4f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.CONTROL_MASK;
    fixtureDef.filter.maskBits = 0;

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createLeftArmUpper(): Body = {
    val bodyDef = new BodyDef()

    bodyDef.position.set(x, 2);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 1.0f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.PLAYER_MASK;
    fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK;

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createLeftArmLower(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.position.set(x, 1);

    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 1.0f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.PLAYER_MASK;
    fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK;

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def createLeftLeg(): Body = {
    val bodyDef = new BodyDef()
    bodyDef.position.set(x, 0.5f);

    bodyDef.`type` = BodyType.StaticBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(0.2f, 0.5f)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape
    fixtureDef.density = 1.0f
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    fixtureDef.filter.categoryBits = CollisionCategory.PLAYER_MASK;
    fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK;

    val fixture = body.createFixture(fixtureDef)

    shape.dispose()
    body
  }

  def attachParts() {
    val elbowDef = new RevoluteJointDef()

    elbowDef.initialize(leftArmUpper, leftArmLower, new Vector2(x, 1.5f))
    elbowDef.enableLimit
    elbowDef.lowerAngle = -0.5f * Math.PI.toFloat
    elbowDef.upperAngle = 0.5f * Math.PI.toFloat

    val shoulderDef = new RevoluteJointDef()

    shoulderDef.initialize(torso, leftArmUpper, new Vector2(x, 2.4f))


    val pelvisJointDef = new RevoluteJointDef()
    pelvisJointDef.initialize(leftLeg, torso, new Vector2(x, 1.0f))

    val neckJointDef = new RevoluteJointDef()
    neckJointDef.initialize(torso, head, new Vector2(x, 3.0f))

    world.createJoint(shoulderDef)
    world.createJoint(elbowDef)
    world.createJoint(pelvisJointDef)
    world.createJoint(neckJointDef)
  }

}
