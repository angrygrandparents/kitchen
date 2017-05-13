package game

import com.badlogic.gdx.Gdx

object Audio {
  lazy val BGM_MENU = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm-menu.mp3"))
  lazy val BGM = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm-twist.mp3"))
  lazy val GRANDPA_HIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-grandpa.mp3"))
  lazy val GRANDMA_HIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-grandma.mp3"))
  lazy val GRANDPA_CRIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-crit-grandpa.mp3"))
  lazy val GRANDMA_CRIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-crit-grandma.mp3"))
  lazy val GEN_HIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-miss.mp3"))
  lazy val GRANDPA_THROW = Gdx.audio.newMusic(Gdx.files.internal("sound/throw-grandpa.mp3"))
  lazy val GRANDMA_THROW = Gdx.audio.newMusic(Gdx.files.internal("sound/throw-grandma.mp3"))
  lazy val THROW = Gdx.audio.newMusic(Gdx.files.internal("sound/woosh.mp3"))
  lazy val VICTORY = Gdx.audio.newMusic(Gdx.files.internal("sound/trumpet.mp3"))
  lazy val CLICK = Gdx.audio.newMusic(Gdx.files.internal("sound/btn-click.wav"))
  lazy val HOVER = Gdx.audio.newMusic(Gdx.files.internal("sound/btn-hover.wav"))

  // make the button hover sound a little less intense
  HOVER.setVolume(0.55f)

  // I wish I could just foreach these
  def disposeAll() = {
    BGM_MENU.dispose()
    BGM.dispose()
    GRANDPA_HIT.dispose()
    GRANDMA_HIT.dispose()
    GRANDPA_CRIT.dispose()
    GRANDMA_CRIT.dispose()
    GEN_HIT.dispose()
    GRANDPA_THROW.dispose()
    GRANDMA_THROW.dispose()
    THROW.dispose()
    VICTORY.dispose()
    CLICK.dispose()
    HOVER.dispose()
  }
}
