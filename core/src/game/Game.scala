package game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.OrthographicCamera;

import game.scene.FightScene


class Game extends ApplicationAdapter with InputProcessor {
  lazy val batch = new SpriteBatch()
	lazy val img = new Texture("badlogic.jpg")

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

	override def create () : Unit = {

	}

	override def render () : Unit = {
    fightScene.update(1/60.0f)
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    fightScene.render(camera)
	}

	override def dispose () : Unit = {
		batch.dispose()
		img.dispose()
	}
  override def mouseMoved(screenX: Int, screenY: Int): Boolean = false


  def keyDown(key: Int): Boolean = {
    Input.keys(key) = true
    true
  }
	def keyTyped(x$1: Char): Boolean = false
  
	def keyUp(key: Int): Boolean = {
    Input.keys(key) = false
    true
  }
	def scrolled(x$1: Int): Boolean = false
	def touchDown(x$1: Int,x$2: Int,x$3: Int,x$4: Int): Boolean = false
	def touchDragged(x$1: Int,x$2: Int,x$3: Int): Boolean = false
	def touchUp(x$1: Int,x$2: Int,x$3: Int,x$4: Int): Boolean = false
}
