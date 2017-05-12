package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class BodyPart(
    isGrandma: Boolean,
    atlas: TextureAtlas,
    spriteId: String,
    world: World,
    position: Vector2,
    dimensions: Vector2,
    spriteOffset: Vector2 = new Vector2(0, 0),
    val drawOrder: Int = 0,
    density: Float = 0.4f,
    isCollider: Boolean = true,
    isItem: Boolean = false,
    colliderId: String = ""
  ) {

  val body = {
    val bodyDef = new BodyDef()
    if (isItem) {
      bodyDef.allowSleep = true
    } else {
      bodyDef.allowSleep = false
    }
    bodyDef.position.set(position)
    bodyDef.`type` = BodyType.DynamicBody

    val body = world.createBody(bodyDef)

    val shape = new PolygonShape()
    shape.setAsBox(dimensions.x, dimensions.y)

    val fixtureDef = new FixtureDef()
    fixtureDef.shape = shape

    fixtureDef.density = density
    fixtureDef.friction = 0.4f
    fixtureDef.restitution = 0.6f
    if (isGrandma) {
      fixtureDef.filter.categoryBits = CollisionCategory.GRANDMA;
      if (isCollider) {
        fixtureDef.filter.maskBits = (CollisionCategory.STATIC_MASK + CollisionCategory.GRANDPA).toShort;
      } else {
        fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK
      }
    } else {
      fixtureDef.filter.categoryBits = CollisionCategory.GRANDPA;
      if (isCollider) {
        fixtureDef.filter.maskBits = (CollisionCategory.STATIC_MASK + CollisionCategory.GRANDMA).toShort;
      } else {
        fixtureDef.filter.maskBits = CollisionCategory.STATIC_MASK
      }
    }
    body.createFixture(fixtureDef)

    if (spriteId != "stick") {
      if (isItem) {
        body.setUserData(CollisionData(true))
      } else {
        body.setUserData(CollisionData(false, isGrandma, spriteId == "head"))
      }
    } else {
      body.setUserData(CollisionData(false, false, false, true))
    }

    shape.dispose()
    body
  }

  val sprite = {
    var s = atlas.createSprite(spriteId)
    s.setOriginCenter()
    s
  }

  def translate(amount: Vector2): Unit = {
    val bodyPos = body.getPosition
    body.setTransform(bodyPos.x + amount.x, bodyPos.y + amount.y, body.getAngle)
  }

  def render(batch: SpriteBatch) {
    val bodyPos = body.getPosition
    val w = sprite.getWidth() / 2.0f
    val h = sprite.getHeight() / 2.0f

    val degrees = body.getAngle / Math.PI.toFloat * 180

    val off = spriteOffset.cpy().rotate(degrees)

    sprite.setPosition((bodyPos.x + off.x) * 48 - w, (bodyPos.y + off.y) * 48 - h)
    sprite.setScale(0.4f, 0.4f)
    sprite.setRotation(degrees)
    sprite.draw(batch)
  }

}
