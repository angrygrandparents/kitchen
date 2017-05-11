package game

import com.badlogic.gdx.Gdx

object Audio {
  val BGM_MENU = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm-menu.mp3"))
  val BGM = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm-twist.mp3"))
  val GRANDPA_HIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-grandpa.wav"))
  val GRANDMA_HIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-grandma.wav"))
  val GRANDPA_CRIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-crit-grandpa.wav"))
  val GRANDMA_CRIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-crit-grandma.wav"))
  val GEN_HIT = Gdx.audio.newMusic(Gdx.files.internal("sound/hit-miss.wav"))
  val GRANDPA_THROW = Gdx.audio.newMusic(Gdx.files.internal("sound/throw-grandpa.wav"))
  val GRANDMA_THROW = Gdx.audio.newMusic(Gdx.files.internal("sound/throw-grandma.wav"))
  val THROW = Gdx.audio.newMusic(Gdx.files.internal("sound/woosh.wav"))
  val VICTORY = Gdx.audio.newMusic(Gdx.files.internal("sound/trumpet.wav"))
  val CLICK = Gdx.audio.newMusic(Gdx.files.internal("sound/btn-click.wav"))
  val HOVER = Gdx.audio.newMusic(Gdx.files.internal("sound/btn-hover.wav"))

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
