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
import game.scene.MainMenuScene

class Game extends ApplicationAdapter with InputProcessor {
  var fightScene = new FightScene()
  lazy val mainMenuScene = new MainMenuScene()

	var gameStarted = false

  lazy val camera = {
    val w = Gdx.graphics.getWidth()
    val h = Gdx.graphics.getHeight()

    val cam = new OrthographicCamera(10, 10 * (h / w.toFloat))

    cam.position.set(0, cam.viewportHeight / 2f, 0);
		cam.update()

    cam
  }

	override def create() : Unit = {
    Audio.BGM_MENU.setLooping(true)
    Audio.BGM_MENU.play()
	}

	override def render() : Unit = {
		val w = Gdx.graphics.getWidth()
		val h = Gdx.graphics.getHeight()

		val ratio = w / h.toFloat;

		val batch = new SpriteBatch()
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(-240 * ratio, 0, 480 * ratio, 480));

		camera.setToOrtho(false, 10 * (w / h.toFloat), 10)
		camera.position.set(0, camera.viewportHeight / 2f, 0);
		camera.update()

		if (mainMenuScene.state == 1) {
			gameStarted = true
			fightScene.dispose()
			fightScene = new FightScene()
      Audio.BGM_MENU.stop()
			mainMenuScene.state = 0
		}


		if (gameStarted) {
			fightScene.update(1/60.0f, camera)
			if (fightScene.goToMenu) {
				if (fightScene.playAgain) {
					fightScene.dispose()
					fightScene = new FightScene()
				} else {
					gameStarted = false
          Audio.BGM_MENU.setLooping(true)
          Audio.BGM_MENU.play()
				}
			}
		} else {
			mainMenuScene.update(1/60.0f, camera)
		}
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    batch.begin()

		if (gameStarted) {
			fightScene.render(batch)
		} else {
			mainMenuScene.render(batch)
		}

		batch.end()

		// camera.setToOrtho(false, 10 * (w / h.toFloat), 10)
		// camera.position.set(0, camera.viewportHeight / 2f, 0);
		// camera.update()
	  // fightScene.renderDebug(camera)

    batch.dispose()
	}

	override def dispose() : Unit = {
		fightScene.dispose()
		mainMenuScene.dispose()
		Audio.disposeAll()
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
