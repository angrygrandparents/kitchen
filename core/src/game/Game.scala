package game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math._
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureAtlas


import game.scene.FightScene


class Game extends ApplicationAdapter with InputProcessor {
	lazy val img = {
    val t = new Texture(Gdx.files.internal("background-01.png"), true)
    t.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest)
    t
  }

  lazy val fightScene = new FightScene()

  lazy val camera = {
    val w = Gdx.graphics.getWidth()
    val h = Gdx.graphics.getHeight()

    println("w: " + w + " h: " + h)

    val cam = new OrthographicCamera(10, 10 * (h / w.toFloat))

    cam.position.set(0, cam.viewportHeight / 2f, 0);
		cam.update()

    cam
  }

	override def create() : Unit = {

	}

	lazy val atlas = new TextureAtlas(Gdx.files.internal("grandma.atlas"));
  lazy val headSpirte = atlas.createSprite("head")
  lazy val bodySprite = atlas.createSprite("body")

	override def render() : Unit = {
    fightScene.update(1/60.0f)
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		val w = Gdx.graphics.getWidth()
    val h = Gdx.graphics.getHeight()

		val ratio = w / h.toFloat;

    val batch = new SpriteBatch()
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(-240 * ratio, 0, 480 * ratio, 480));

		camera.setToOrtho(false, 10 * (w / h.toFloat), 10)
		camera.position.set(0, camera.viewportHeight / 2f, 0);
		camera.update()

    batch.begin()

    val width = 480 * 8/4.5f;

    batch.draw(img, -(width - 480 * ratio) / 2 - 240 * ratio, 0, width, 480, 0, 1, 1, 0)

    fightScene.render(batch)

		batch.end()

	  fightScene.renderDebug(camera)


    batch.dispose()
	}

	override def dispose () : Unit = {
		img.dispose()
	}
  override def mouseMoved(screenX: Int, screenY: Int): Boolean = false


  def keyDown(key: Int): Boolean = false
	def keyTyped(x$1: Char): Boolean = false

	def keyUp(key: Int): Boolean = false
	def scrolled(x$1: Int): Boolean = false
	def touchDown(x$1: Int,x$2: Int,x$3: Int,x$4: Int): Boolean = false
	def touchDragged(x$1: Int,x$2: Int,x$3: Int): Boolean = false
	def touchUp(x$1: Int,x$2: Int,x$3: Int,x$4: Int): Boolean = false
}
