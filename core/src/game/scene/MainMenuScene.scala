package game.scene

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Color

class MainMenuScene {
  lazy val background = {
    val t = new Texture(Gdx.files.internal("menubackground.png"), true)
    t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear)
    t
  }

  lazy val tutorialBackground = {
    val t = new Texture(Gdx.files.internal("tutorialbackground.png"), true)
    t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear)
    t
  }

  lazy val creditsBackground = {
    val t = new Texture(Gdx.files.internal("creditsbackground.png"), true)
    t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear)
    t
  }

  lazy val storyBackground = {
    val t = new Texture(Gdx.files.internal("open story.png"), true)
    t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear)
    t
  }

  lazy val atlas = new TextureAtlas(Gdx.files.internal("menu.atlas"))
  lazy val tutorialatlas = new TextureAtlas(Gdx.files.internal("tutorial.atlas"))

  lazy val playButton = new Button(atlas, "play", new Vector2(0, 4.7f))
  lazy val tutorialButton = new Button(atlas, "tutorial", new Vector2(0, 3.5f))
  lazy val creditsButton = new Button(atlas, "credits", new Vector2(0, 2.3f))
  lazy val exitButton = new Button(atlas, "exit", new Vector2(0, 1.1f))

  lazy val tutorialBackButton = new Button(tutorialatlas, "back", new Vector2(0, 1.25f))

  var state = 4

  var storyTimer = 0.0f
  val frameDuration = 3.0f

  val whitemap = new Pixmap(1, 1, Pixmap.Format.RGBA8888)
  whitemap.setColor(new Color(1, 1, 1, 1))
  whitemap.fillRectangle(0, 0, 1, 1)
  val whiteTex = new Texture(whitemap)

  def render(batch: SpriteBatch) {
    val w = Gdx.graphics.getWidth()
    val h = Gdx.graphics.getHeight()
		val ratio = w / h.toFloat;
    val width = 480 * 8/4.5f;

    batch.draw(background, -(width - 480 * ratio) / 2 - 240 * ratio, 0, width, 480, 0, 1, 1, 0)

    if (state == 0) {
      playButton.render(batch)
      tutorialButton.render(batch)
      creditsButton.render(batch)
      exitButton.render(batch)
    } else if (state == 2) {
      val tWidth = 440 * 1833/1033f;
      batch.draw(tutorialBackground, -(tWidth - 480 * ratio) / 2 - 240 * ratio, 20, tWidth, 440, 0, 1, 1, 0)
      tutorialBackButton.render(batch)
    } else if (state == 3) {
      val tWidth = 440 * 1066/948f;
      batch.draw(creditsBackground, -(tWidth - 480 * ratio) / 2 - 240 * ratio, 20, tWidth, 440, 0, 1, 1, 0)
      tutorialBackButton.render(batch)
    } else if (state == 4) {
      val tWidth = 480 * 1920/1080f;
      val frame = (storyTimer / frameDuration).toInt + 1
      val diff = storyTimer - (frame - 1) * frameDuration
      val a = math.min(diff, 1.0f)

      if (frame > 1 && frame < 6) {
        batch.draw(storyBackground, -(tWidth - 480 * ratio) / 2 - 240 * ratio, 0, tWidth, 480, 0, 0.25f * (frame - 1), 1, 0.25f * (frame - 2))
        batch.setColor(1.0f, 1.0f, 1.0f, a)
      }

      if (frame < 5) {
        batch.draw(storyBackground, -(tWidth - 480 * ratio) / 2 - 240 * ratio, 0, tWidth, 480, 0, 0.25f * frame, 1, 0.25f * (frame - 1))
      } else {
        batch.setColor(1.0f, 1.0f, 1.0f, a)
        batch.draw(whiteTex, -w/2, 0, w, 480, 0, 1, 1, 0)
        if (a == 1.0f) {
          val a2 = math.min(diff - 1.0f, 1.0f)
          batch.setColor(1.0f, 1.0f, 1.0f, a2)
          batch.draw(background, -(width - 480 * ratio) / 2 - 240 * ratio, 0, width, 480, 0, 1, 1, 0)
          playButton.render(batch)
          tutorialButton.render(batch)
          creditsButton.render(batch)
          exitButton.render(batch)
        }
      }
    }
  }

  def update(delta: Float, camera: Camera) {
    if (state == 2 || state == 3) {
      tutorialBackButton.update(delta, camera)
      if (tutorialBackButton.clicked) {
        state = 0
        tutorialBackButton.clicked = false
      }
    } else if (state == 4) {
      storyTimer += delta
      if (storyTimer >= frameDuration * 5) {
        state = 0
      }
      if (Gdx.input.isTouched()) {
        state = 0
      }
    } else {
      storyTimer = 0.0f
      playButton.update(delta, camera)
      tutorialButton.update(delta, camera)
      creditsButton.update(delta, camera)
      exitButton.update(delta, camera)
      if (playButton.clicked) {
        state = 1
        playButton.clicked = false
      } else if (tutorialButton.clicked) {
        state = 2
        tutorialButton.clicked = false
      } else if (creditsButton.clicked) {
        state = 3
        creditsButton.clicked = false
      } else if (exitButton.clicked) {
        Gdx.app.exit()
      }
    }
  }

  def dispose() : Unit = {
		background.dispose()
	}

}
