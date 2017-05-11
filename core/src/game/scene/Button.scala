package game.scene

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera


class Button(atlas: TextureAtlas, id: String, position: Vector2) {
  val normalSprite = atlas.createSprite(id+"-normal")
  val selectSprite = atlas.createSprite(id+"-select")
  val clickSprite = atlas.createSprite(id+"-click")

  var state = 0

  var clicked = false

  var touched = true

  def getMousePosInGameWorld(camera: Camera): Vector3 = {
    camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
  }

  def update(delta: Float, camera: Camera) {
    val w = normalSprite.getWidth() / 48.0f / 2 * 0.4f
    val h = normalSprite.getHeight() / 48.0f / 2 * 0.4f

    val mousePos = getMousePosInGameWorld(camera)

    val posX = position.x
    val posY = position.y

    val isX = ((mousePos.x - position.x) / w).abs < 1.0f
    val isY = ((mousePos.y - position.y) / h).abs < 1.0f

    if (isX && isY) {
      val isTouched = Gdx.input.isTouched()
      val prevState = state
      state = if (isTouched) 2 else 1
      if (prevState == 2 && state == 1) {
        clicked = true
      }
    } else {
      state = 0
    }
  }

  def render(batch: SpriteBatch) {
    val sprite = state match {
      case 1 => selectSprite
      case 2 => clickSprite
      case _ => normalSprite
    }

    val w = sprite.getWidth() / 2.0f
    val h = sprite.getHeight() / 2.0f

    sprite.setPosition(position.x * 48 - w, position.y * 48 - h)
    sprite.setScale(0.4f, 0.4f)
    sprite.draw(batch)
  }
}
