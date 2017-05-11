package game.scene

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Set

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.math._
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas

import game.Constant
import game.physics.Player
import game.physics.CollisionCategory
import game.physics.BodyPart
import game.physics.CollisionData
import game.effect.Smoke

class CollisionListener extends ContactListener {
  val neutralCollisionIds = Set[Long]()
  val grandmaCollisionIds = Set[Long]()
  val grandmaCritCollisionIds = Set[Long]()
  val grandpaCollisionIds = Set[Long]()
  val grandpaCritCollisionIds = Set[Long]()

  override def beginContact(contact: Contact) : Unit = {

  }

  override def endContact(contact: Contact) : Unit = {
    val fixtureA = contact.getFixtureA()
    val fixtureB = contact.getFixtureB()

    val bodA = fixtureA.getBody()
    val bodB = fixtureB.getBody()

    val userDataA = Option(bodA.getUserData())
    val userDataB = Option(bodB.getUserData())

    if (userDataA.isDefined) {
      val collisionData = userDataA.get.asInstanceOf[CollisionData]
      if (collisionData.isItem) {
        if (userDataB.isDefined) {
          val use = userDataB.get.asInstanceOf[CollisionData]
          if (use.isItem) {
            neutralCollisionIds += collisionData.id
          } else {
            if (use.isGrandma) {
              if (use.isCritical) {
                grandmaCritCollisionIds += collisionData.id
              } else {
                grandmaCollisionIds += collisionData.id
              }
            } else {
              if (use.isCritical) {
                grandpaCritCollisionIds += collisionData.id
              } else {
                grandpaCollisionIds += collisionData.id
              }
            }
          }
        } else {
          neutralCollisionIds += collisionData.id
        }
      }
    }

    if (userDataB.isDefined) {
      val collisionData = userDataB.get.asInstanceOf[CollisionData]
      if (collisionData.isItem) {
        if (userDataA.isDefined) {
          val use = userDataA.get.asInstanceOf[CollisionData]
          if (use.isItem) {
            neutralCollisionIds += collisionData.id
          } else {
            if (use.isGrandma) {
              if (use.isCritical) {
                grandmaCritCollisionIds += collisionData.id
              } else {
                grandmaCollisionIds += collisionData.id
              }
            } else {
              if (use.isCritical) {
                grandpaCritCollisionIds += collisionData.id
              } else {
                grandpaCollisionIds += collisionData.id
              }
            }
          }
        } else {
          neutralCollisionIds += collisionData.id
        }
      }
    }
    // Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString())
  }

  override def preSolve(contact: Contact, oldManifold: Manifold) : Unit = { }

  override def postSolve(contact: Contact, impulse: ContactImpulse) : Unit = { }

}

class Lifebar(isGrandma: Boolean) {

  val t = if (isGrandma) {
    "grandma"
  } else {
    "grandpa"
  }

  val sign = if (isGrandma) {
    1.0f
  } else {
    -1.0f
  }

  val scale = 48.0f

  lazy val atlas = new TextureAtlas(Gdx.files.internal("HP-" + t + ".atlas"))

  lazy val lifebar = {
    val s = atlas.createSprite("lifebar-background")
    val w = s.getWidth() / 2.0f
    val h = s.getHeight() / 2.0f
    s.setPosition(sign * -4.75f * scale - w, 8.75f * scale - h)
    s.setScale(0.5f, 0.5f)
    s
  }

  lazy val hpIcons = for (i <- 1 to 10) yield {
    val t = if (i < 10) {
      "0" + i
    } else {
      "10"
    }
    val s = atlas.createSprite("HP-" + t)
    val w = s.getWidth() / 2.0f
    val h = s.getHeight() / 2.0f
    s.setPosition(sign * (-3.03f - (i - 1)*(0.384f)) * scale - w, 8.75f * scale - h)
    s.setScale(0.5f, 0.5f)
    s
  }

  lazy val icon = if (isGrandma) {
    val s = atlas.createSprite("grandma-icon")
    val w = s.getWidth() / 2.0f
    val h = s.getHeight() / 2.0f
    s.setPosition(sign * -7.25f * scale - w, 9f * scale - h)
    s.setScale(0.5f, 0.5f)
    s
  } else {
    val s = atlas.createSprite("grandpa-icon")
    val w = s.getWidth() / 2.0f
    val h = s.getHeight() / 2.0f
    s.setPosition(sign * -7.25f * scale - w, 8.85f * scale - h)
    s.setScale(0.5f, 0.5f)
    s
  }

  def render(batch: SpriteBatch, lifeLeft: Float) {
    lifebar.draw(batch)
    for (i <- 0 until lifeLeft.ceil.toInt) {
      hpIcons(9 - i).draw(batch)
    }
    icon.draw(batch)
  }
}


class FightScene {
  val world = new World(new Vector2(0, -6), true)

  val grandmaLifebar = new Lifebar(true)
  val grandpaLifebar = new Lifebar(false)

  val collisionListener = new CollisionListener()
  world.setContactListener(collisionListener)

  val ground = createGround()
  val playerA = new Player(world, 1, ground)
  val playerB = new Player(world, 2, ground)

  val items = new ArrayBuffer[BodyPart]()

  val smokes = new ArrayBuffer[Smoke]()

  val debugRenderer = new Box2DDebugRenderer()

  private var accumulator = 0.0f

  def playGrandmaIsHitSound(isCritical: Boolean) {
    // TODO
  }

  def playGrandpaIsHitSound(isCritical: Boolean) {
    // TODO
  }

  def playHitOtherSound() {
    // TODO
  }



  def update(delta: Float) {
    var grandmaDamage = 0.0f
    var grandpaDamage = 0.0f


    collisionListener.neutralCollisionIds.foreach { id =>
      val item = items.find { item =>
        item.body.getUserData().asInstanceOf[CollisionData].id == id
      }
      if (item.isDefined) {
        val pos = item.get.body.getPosition
        world.destroyBody(item.get.body)
        items -= item.get
        smokes += new Smoke(pos.cpy(), 0)
        playHitOtherSound()
      }
    }

    collisionListener.grandmaCollisionIds.foreach { id =>
      val item = items.find { item =>
        item.body.getUserData().asInstanceOf[CollisionData].id == id
      }
      if (item.isDefined) {
        val pos = item.get.body.getPosition
        world.destroyBody(item.get.body)
        items -= item.get
        smokes += new Smoke(pos.cpy(), 1)
        grandmaDamage += 1.0f
        playGrandmaIsHitSound(false)
      }
    }

    collisionListener.grandmaCritCollisionIds.foreach { id =>
      val item = items.find { item =>
        item.body.getUserData().asInstanceOf[CollisionData].id == id
      }
      if (item.isDefined) {
        val pos = item.get.body.getPosition
        world.destroyBody(item.get.body)
        items -= item.get
        smokes += new Smoke(pos.cpy(), 2)
        grandmaDamage += 2.0f
        playGrandmaIsHitSound(true)
      }
    }

    collisionListener.grandpaCollisionIds.foreach { id =>
      val item = items.find { item =>
        item.body.getUserData().asInstanceOf[CollisionData].id == id
      }
      if (item.isDefined) {
        val pos = item.get.body.getPosition
        world.destroyBody(item.get.body)
        items -= item.get
        smokes += new Smoke(pos.cpy(), 1)
        grandpaDamage += 1.0f
        playGrandpaIsHitSound(false)
      }
    }

    collisionListener.grandpaCritCollisionIds.foreach { id =>
      val item = items.find { item =>
        item.body.getUserData().asInstanceOf[CollisionData].id == id
      }
      if (item.isDefined) {
        val pos = item.get.body.getPosition
        world.destroyBody(item.get.body)
        items -= item.get
        smokes += new Smoke(pos.cpy(), 2)
        grandpaDamage += 2.0f
        playGrandpaIsHitSound(true)
      }
    }

    collisionListener.neutralCollisionIds.clear()
    collisionListener.grandmaCollisionIds.clear()
    collisionListener.grandpaCollisionIds.clear()

    if (grandmaDamage > 0) {
      playerA.takeDamage(grandmaDamage)
    }
    if (grandpaDamage > 0) {
      playerB.takeDamage(grandpaDamage)
    }


    playerA.update(delta, items)
    playerB.update(delta, items)
    smokes.foreach { smoke =>
      smoke.update(delta)
    }
    smokes.filter { !_.isDone }
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

    smokes.foreach { smoke =>
      smoke.render(batch)
    }

    items.foreach { item =>
      item.render(batch)
    }

    grandmaLifebar.render(batch, playerA.health)
    grandpaLifebar.render(batch, playerB.health)
  }

  def renderDebug(camera: Camera) {
    debugRenderer.render(world, camera.combined)
  }

}
