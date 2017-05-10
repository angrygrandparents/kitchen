package game.scene

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.math._
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import game.Constant
import game.physics.Player
import game.physics.CollisionCategory



class FightScene {
  val world = new World(new Vector2(0, -10), true)

  val ground = createGround()
  val playerA = new Player(world, 1, ground)
  val playerB = new Player(world, 2, ground)

  val debugRenderer = new Box2DDebugRenderer()

  private var accumulator = 0.0f

  def update(delta: Float) {
    playerA.update()
    playerB.update()
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

  def createGround(): Body = {
    val groundBodyDef = new BodyDef()

    groundBodyDef.position.set(new Vector2(0, 0))

    val groundBody = world.createBody(groundBodyDef)

    val groundBox = new PolygonShape()
    groundBox.setAsBox(99.0f, 0.25f)
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

    groundBody
  }

  def render(batch: SpriteBatch) {
    playerA.render(batch)
    playerB.render(batch)
  }

  def renderDebug(camera: Camera) {
    debugRenderer.render(world, camera.combined)
  }

}
