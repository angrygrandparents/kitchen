package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils

import scala.collection.mutable.ArrayBuffer

import game.Input
import game.Audio

object Player {
  lazy val objectAtlas = new TextureAtlas(Gdx.files.internal("objects.atlas"))
}

class ItemDispenser(offset: Vector2, isGrandma: Boolean, world: World) {
  val atlas = Player.objectAtlas

  var item: Option[BodyPart] = None

  var timer = 0.0f

  def update(delta: Float) {
    if (item.isEmpty) {
      timer += delta
      if (timer > 2.0f) {
        val r = MathUtils.random(0, 6)
        item = r match {
          case 0 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-01", world, offset, new Vector2(0.7f, 0.25f), new Vector2(0, 0), 20, 0.1f, true, true)) // Rolling Pin
          case 1 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-02", world, offset, new Vector2(0.9f, 0.15f), new Vector2(0, 0), 20, 0.1f, true, true)) // Plumbers thing
          case 2 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-03", world, offset, new Vector2(0.35f, 0.35f), new Vector2(0, 0), 20, 0.1f, true, true)) // Donut
          case 3 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-04", world, offset, new Vector2(0.8f, 0.35f), new Vector2(0, 0), 20, 0.1f, true, true)) // Pineapple
          case 4 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-05", world, offset, new Vector2(0.55f, 0.15f), new Vector2(0, 0), 20, 0.1f, true, true)) // Carrot
          case 5 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-06", world, offset, new Vector2(1.2f, 0.25f), new Vector2(0, 0), 20, 0.1f, true, true)) // Guitar
          case 6 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-07", world, offset, new Vector2(0.6f, 0.5f), new Vector2(0, 0), 20, 0.1f, true, true)) // Kettle
          case 7 => Some(new BodyPart(isGrandma, Player.objectAtlas, "objects-08", world, offset, new Vector2(1.3f, 0.8f), new Vector2(0, 0), 20, 0.07f, true, true)) // Microwave
        }
        item.get.body.setActive(false)
        timer = 0.0f
      }
    }
  }

  def render(batch: SpriteBatch) {
    if (item.isDefined) {
      item.get.render(batch)
    }
  }

  def hasItem : Boolean = item.isDefined

  def dispense() : BodyPart = {
    val i = item.get
    item = None
    i
  }

}


class Player(world: World, playerNumber: Int, groundBody: Body) {

  lazy val atlas = {
    if (playerNumber == 1) {
      new TextureAtlas(Gdx.files.internal("grandma.atlas"));
    } else {
      new TextureAtlas(Gdx.files.internal("grandpa.atlas"));
    }
  }

  val isGrandma = playerNumber == 1

  val sign = if (playerNumber == 1) 1.0f else -1.0f

  val itemDispenser = new ItemDispenser(new Vector2(-6.25f * sign, 5.0f), isGrandma, world)

  val MAX_HITSTUN = 1.2f

  var hitstunTimer = 0.0f
  var isInHitstun = false

  val body = {
    val body = new CharacterBody(world)

    val torso = new BodyPart(isGrandma, atlas, ("body"), world, new Vector2(sign * 0, 3.5f), new Vector2(0.8f, 1.0f), new Vector2(0, 0.3f))

    val head = {
      if (playerNumber == 1) {
        new BodyPart(isGrandma, atlas, ("head"), world, new Vector2(sign * 0.1f, 4.9f), new Vector2(0.5f, 0.4f), new Vector2(sign * -0.1f, 0.4f))
      } else {
        new BodyPart(isGrandma, atlas, ("head"), world, new Vector2(sign * 0.1f, 4.9f), new Vector2(0.5f, 0.4f), new Vector2(-0.1f, -0.1f))
      }
    }

    val leftUpperArm = new BodyPart(isGrandma, atlas, ("UpperArm-L"), world, new Vector2(sign * -1.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), 10, 0.4f, false)
    val leftLowerArm = new BodyPart(isGrandma, atlas, ("LowerArm-L"), world, new Vector2(sign * -2.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), 10, 0.4f, false)

    val rightUpperArm = new BodyPart(isGrandma, atlas, ("UpperArm-R"), world, new Vector2(sign * 1.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), -13, 0.4f, false)
    val rightLowerArm = new BodyPart(isGrandma, atlas, ("LowerArm-R"), world, new Vector2(sign * 2.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), -13, 0.4f, false)

    val cane = new BodyPart(isGrandma, atlas, "stick", world, new Vector2(sign * 2.7f, 3.4f), new Vector2(0.1f, 0.8f), new Vector2(0, 0), -12)

    val leftLeg = new BodyPart(isGrandma, atlas, ("leg_L"), world, new Vector2(sign * -0.6f, 2.0f), new Vector2(0.1f, 0.8f), new Vector2(0, 0), -10, 0.4f, false)
    val rightLeg = new BodyPart(isGrandma, atlas, ("leg_R"), world, new Vector2(sign * 0.6f, 2.0f), new Vector2(0.1f, 0.8f), new Vector2(0, 0), -10, 0.4f, false)

    body.addBodyPart("torso", torso)

    body.addBodyPart("head", head)

    body.addBodyPart("leftUpperArm", leftUpperArm)
    body.addBodyPart("leftLowerArm", leftLowerArm)

    body.addBodyPart("rightUpperArm", rightUpperArm)
    body.addBodyPart("rightLowerArm", rightLowerArm)

    body.addBodyPart("leftLeg", leftLeg)
    body.addBodyPart("rightLeg", rightLeg)

    body.addBodyPart("cane", cane)

    body.connect("leftShoulder", "leftUpperArm", "torso", new Vector2(sign * -0.7f, 4.2f))
    body.connect("leftElbow", "leftUpperArm", "leftLowerArm", new Vector2(sign * -1.7f, 4.2f))

    body.connect("rightShoulder", "rightUpperArm", "torso", new Vector2(sign * 0.7f, 4.2f))
    body.connect("rightElbow", "rightUpperArm", "rightLowerArm", new Vector2(sign * 1.7f, 4.2f))

    body.connect("cane", "cane", "rightLowerArm", new Vector2(sign * 2.7f, 4.2f))

    if (isGrandma) {
      body.setLimits("rightShoulder", -Math.PI.toFloat / 2, 2 * Math.PI.toFloat / 3)
    } else {
      body.setLimits("rightShoulder", -2 * Math.PI.toFloat / 3, Math.PI.toFloat / 2)
    }

    body.connect("leftHip", "leftLeg", "torso", new Vector2(sign * -0.6f, 2.7f))
    body.connect("rightHip", "rightLeg", "torso", new Vector2(sign * 0.6f, 2.7f))

    body.connect("neck", "head", "torso", new Vector2(sign * 0, 4.5f))

    body.translate(new Vector2(sign * -3.0f, -0.9f))

    body.connect("ground", "leftLeg", groundBody, new Vector2(sign * -3.6f, 0.25f))

    body
  }

  var holding = false
  var prepThrow = false

  var health = 10.0f

  def grabItem() : Unit = {
    val lowerArm = body.getBodyPart("leftLowerArm")
    val pos = lowerArm.body.getPosition()
    val angle = lowerArm.body.getAngle() / Math.PI.toFloat * 180

    val off = (new Vector2(sign * -0.6f, 0)).rotate(angle)
    off.add(pos)

    if (off.cpy().sub(new Vector2(-6.25f * sign, 5.0f)).len2 < 4.0f) {

      if (itemDispenser.hasItem) {
        val part = itemDispenser.dispense() //new BodyPart(isGrandma, Player.objectAtlas, "objects-01", world, off, new Vector2(0.8f, 0.35f), new Vector2(0, 0), 20, 0.1f, true, true)

        part.body.setActive(true)

        part.body.setTransform(off.x, off.y, 0)

        body.addBodyPart("item", part)
        body.connect("item", "leftLowerArm", "item", off)
        holding = true
      }
    }
  }

    def releaseItem(items: ArrayBuffer[BodyPart]) : Unit = {
    body.removeJoint("item")
    holding = false
    prepThrow = false
    if (isGrandma) {
      Audio.GRANDMA_THROW.play()
    } else {
      Audio.GRANDPA_THROW.play()
    }
    Audio.THROW.play()
    val item = body.removeBodyPart("item")
    items += item
  }

  def takeDamage(amount: Float) : Unit = {
    if (amount > 1.0f && !isInHitstun) {
      isInHitstun = true
      hitstunTimer = 0.0f
    }
    health -= amount
  }

  def update(delta: Float, items: ArrayBuffer[BodyPart]) : Unit = {
    itemDispenser.update(delta)
    body.setAngleTarget("neck", 0, 80.0f)
    body.setAngleTarget("ground", 0, 40.0f)
    body.setAngleTarget("leftElbow", sign * Math.PI.toFloat / 6)

    body.setAngleTarget("rightElbow", sign * Math.PI.toFloat / 3)
    body.setAngleTarget("cane", 0, 20.0f)

    body.relaxJoint("leftShoulder")
    body.relaxJoint("rightShoulder")
    body.setAngleTarget("leftHip", 0, 30.0f)
    body.setAngleTarget("rightHip", 0, 0.8f)

    if (isInHitstun) {
      hitstunTimer += delta
      if (hitstunTimer > MAX_HITSTUN) {
        isInHitstun = false
      }
    }

    if (health <= 0) {
      isInHitstun = true
    }

    if (isInHitstun) {
      if (hitstunTimer < 0.1f) {
        body.setAngleTarget("ground", sign * Math.PI.toFloat / 2, 40.0f, 10.0f)
        body.setAngleTarget("leftHip", sign * Math.PI.toFloat / 3, 10.0f, 4.0f)
      } else {
        body.relaxJoint("ground")
        body.relaxJoint("leftHip")
      }
      body.setAngleTarget("neck", sign * -Math.PI.toFloat / 12, 80.0f)
      body.relaxJoint("rightHip")
      body.relaxJoint("ground")
    } else {

      val moveCaneUp = if (playerNumber == 1) {
        Gdx.input.isKeyPressed(Keys.W)
      } else {
        Gdx.input.isKeyPressed(Keys.UP)
      }

      val moveCaneDown = if (playerNumber == 1) {
        Gdx.input.isKeyPressed(Keys.S)
      } else {
        Gdx.input.isKeyPressed(Keys.DOWN)
      }

      val leanBack = if (playerNumber == 1) {
        Gdx.input.isKeyPressed(Keys.A)
      } else {
        Gdx.input.isKeyPressed(Keys.RIGHT)
      }

      val leanForward = if (playerNumber == 1) {
        Gdx.input.isKeyPressed(Keys.D)
      } else {
        Gdx.input.isKeyPressed(Keys.LEFT)
      }


      if (leanBack) {
        prepThrow = false
        if (!holding) {
          grabItem()
        }
        body.setAngleTarget("leftShoulder", sign * Math.PI.toFloat / 8, 8.0f)
        body.setAngleTarget("leftElbow", sign * -Math.PI.toFloat / 4)
        body.setAngleTarget("leftHip", sign * Math.PI.toFloat / 5, 30.0f)
        body.setAngleTarget("rightHip", sign * -Math.PI.toFloat / 3, 3.0f)

        body.setAngleTarget("rightShoulder", sign * Math.PI.toFloat / 3)

        body.setAngleTarget("cane", sign * -Math.PI.toFloat / 4, 20.0f)

      } else if (leanForward) {
        if (!holding) {
          grabItem()
        }
        if (holding) {
          prepThrow = true
        }
        body.setAngleTarget("leftShoulder", 5 * sign * Math.PI.toFloat / 6, 6.0f, 2.0f)
        body.setAngleTarget("leftElbow", 0, 3.0f)
        body.setAngleTarget("leftHip", sign * Math.PI.toFloat / 6, 30.0f, 8.0f)
        body.setAngleTarget("rightHip", sign * 0, 20.0f)
        body.setAngleTarget("ground", sign * Math.PI.toFloat / 6, 50.0f)

        body.setAngleTarget("rightShoulder", sign * Math.PI.toFloat / 3, 20.0f)

      }

      if (moveCaneUp) {
        body.setAngleTarget("rightShoulder", sign * -Math.PI.toFloat / 3, 10.0f)
        body.setAngleTarget("cane", sign * -Math.PI.toFloat / 3, 2.0f)
      }

      if (holding) {
        val lowerArm = body.getBodyPart("leftLowerArm")
        val angle = lowerArm.body.getAngle()
        if (leanForward)
          body.setAngleTarget("item", angle - Math.PI.toFloat/2, 10.0f, 1.0f)
        else {
          body.relaxJoint("item")
        }
      }
      if (prepThrow && !leanForward) {
        releaseItem(items)
      }
    }

  }

  def render(batch: SpriteBatch) : Unit = {
    itemDispenser.render(batch)
    body.render(batch)
  }


}
