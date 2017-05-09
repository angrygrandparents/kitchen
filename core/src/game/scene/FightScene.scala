package game.scene

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.math._
import com.badlogic.gdx.graphics.Camera

import game.Constant
import game.physics.Player
import game.physics.CollisionCategory



class FightScene {
  val world = new World(new Vector2(0, -10), true)

  createGround()
  val playerA = new Player(world)

  val debugRenderer = new Box2DDebugRenderer()

  private var accumulator = 0.0f

  def update(delta: Float) {
    world.step(delta, 6, 2)
    val frameTime = Math.min(delta, 0.25f)
    accumulator += frameTime
    while (accumulator >= Constant.TIME_STEP) {
      world.step(
        Constant.TIME_STEP,
        Constant.VELOCITY_ITERATIONS,
        Constant.POSITION_ITERATIONS)

      accumulator -= Constant.TIME_STEP
    }

  }

  def createGround() {
    val groundBodyDef = new BodyDef()

    groundBodyDef.position.set(new Vector2(0, -0.5f))

    val groundBody = world.createBody(groundBodyDef)

    val groundBox = new PolygonShape()
    groundBox.setAsBox(99.0f, 0.5f)
    groundBody.createFixture(groundBox, 0.0f)

    val fixtureDef = new FixtureDef();
    fixtureDef.shape = groundBox;
    fixtureDef.density = 0.0f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0.6f; // Make it bounce a little bit
    fixtureDef.filter.categoryBits = CollisionCategory.STATIC_MASK;
    // fixtureDef.filter.maskBits = CollisionCategory.PLAYER_MASK;

    groundBody.createFixture(fixtureDef)

    groundBox.dispose()
  }

  def createBall() {
    val bodyDef = new BodyDef()
    // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
    bodyDef.`type` = BodyType.DynamicBody
    // Set our body's starting position in the world
    bodyDef.position.set(0, 100);

    // Create our body in the world using our body definition
    val body = world.createBody(bodyDef);

    // Create a circle shape and set its radius to 6
    val circle = new CircleShape();
    circle.setRadius(6f);

    // Create a fixture definition to apply our shape to
    val fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0.6f

    // Create our fixture and attach it to the body
    val fixture = body.createFixture(fixtureDef)

    // Remember to dispose of any shapes after you're done with them!
    // BodyDef and FixtureDef don't need disposing, but shapes do.
    circle.dispose();
  }

  def render(camera: Camera) {
    debugRenderer.render(world, camera.combined)
  }

}
