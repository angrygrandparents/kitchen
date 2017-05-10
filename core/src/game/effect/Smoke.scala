package game.effect

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.g2d.SpriteBatch

object Smoke {
  lazy val blueSmokeAtlas = new TextureAtlas(Gdx.files.internal("smoke-blue.atlas"));
  lazy val pinkSmokeAtlas = new TextureAtlas(Gdx.files.internal("smoke-pink.atlas"));
  lazy val greySmokeAtlas = new TextureAtlas(Gdx.files.internal("smoke-grey.atlas"));

  lazy val blueFrames = Vector(
    "smoke-blue-01",
    "smoke-blue-02",
    "smoke-blue-03",
    "smoke-blue-04",
    "smoke-blue-05",
    "smoke-blue-06",
    "smoke-blue-07",
    "smoke-blue-08",
    "smoke-blue-09"
  )

  lazy val pinkFrames = Vector(
    "smoke-pink-01",
    "smoke-pink-02",
    "smoke-pink-03",
    "smoke-pink-04",
    "smoke-pink-05",
    "smoke-pink-06",
    "smoke-pink-07",
    "smoke-pink-08",
    "smoke-pink-09"
  )

  lazy val greyFrames = Vector(
    "smoke-grey-01",
    "smoke-grey-02",
    "smoke-grey-03",
    "smoke-grey-04",
    "smoke-grey-05",
    "smoke-grey-06",
    "smoke-grey-07",
    "smoke-grey-08",
    "smoke-grey-09"
  )
}

class Smoke(position: Vector2, color: Int) {

  val frameDuration = 0.05f

  val sprites = {
    color match {
      case 1 => {
        Smoke.blueFrames.map { id =>
          val s = Smoke.blueSmokeAtlas.createSprite(id)
          s.setOriginCenter()
          s
        }
      }
      case 2 => {
        Smoke.pinkFrames.map { id =>
          val s = Smoke.pinkSmokeAtlas.createSprite(id)
          s.setOriginCenter()
          s
        }
      }
      case _ => {
        Smoke.greyFrames.map { id =>
          val s = Smoke.greySmokeAtlas.createSprite(id)
          s.setOriginCenter()
          s
        }
      }
    }
  }

  var time = 0.0f

  def isDone: Boolean = {
    time >= (sprites.length - 1) * frameDuration
  }

  def update(delta: Float) {
    time += delta
  }

  def render(batch: SpriteBatch) {
    if (!isDone) {
      val frame = (time / frameDuration).toInt
      val sprite = sprites(frame)

      val w = sprite.getWidth() / 2.0f
      val h = sprite.getHeight() / 2.0f

      sprite.setPosition(position.x * 48 - w, position.y * 48 - h)
      sprite.setScale(0.4f, 0.4f)
      sprite.draw(batch)
    }

  }

}
