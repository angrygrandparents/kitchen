package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import scala.collection.mutable.ArrayBuffer

import game.Input

object Player {
  lazy val objectAtlas = new TextureAtlas(Gdx.files.internal("objects.atlas"))
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

  val body = {
    val body = new CharacterBody(world)

    val torso = new BodyPart(isGrandma, atlas, ("body"), world, new Vector2(sign * 0, 3.5f), new Vector2(0.8f, 1.0f), new Vector2(0, 0.3f))

    val head = {
      if (playerNumber == 1) {
        new BodyPart(isGrandma, atlas, ("head"), world, new Vector2(sign * 0.1f, 4.9f), new Vector2(0.7f, 0.5f), new Vector2(sign * -0.1f, 0.4f))
      } else {
        new BodyPart(isGrandma, atlas, ("head"), world, new Vector2(sign * 0.1f, 4.9f), new Vector2(0.7f, 0.5f), new Vector2(-0.1f, -0.1f))
      }
    }

    val leftUpperArm = new BodyPart(isGrandma, atlas, ("UpperArm-L"), world, new Vector2(sign * -1.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), 10, 0.4f, false)
    val leftLowerArm = new BodyPart(isGrandma, atlas, ("LowerArm-L"), world, new Vector2(sign * -2.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), 10, 0.4f, false)

    val rightUpperArm = new BodyPart(isGrandma, atlas, ("UpperArm-R"), world, new Vector2(sign * 1.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), -10, 0.4f, false)
    val rightLowerArm = new BodyPart(isGrandma, atlas, ("LowerArm-R"), world, new Vector2(sign * 2.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), -10, 0.4f, false)

    val cane = new BodyPart(isGrandma, atlas, "stick", world, new Vector2(sign * 2.7f, 3.4f), new Vector2(0.1f, 0.8f), new Vector2(0, 0), -8)

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

    body.connect("leftHip", "leftLeg", "torso", new Vector2(sign * -0.6f, 2.7f))
    body.connect("rightHip", "rightLeg", "torso", new Vector2(sign * 0.6f, 2.7f))

    body.connect("neck", "head", "torso", new Vector2(sign * 0, 4.5f))

    body.setAngleTarget("neck", 0)

    body.translate(new Vector2(sign * -5.0f, -0.9f))

    body.connect("ground", "leftLeg", groundBody, new Vector2(sign * -5.6f, 0.25f))

    body
  }

  var holding = false
  var prepThrow = false

  def grabItem() : BodyPart = {
    val lowerArm = body.getBodyPart("leftLowerArm")
    val pos = lowerArm.body.getPosition()
    val angle = lowerArm.body.getAngle() / Math.PI.toFloat * 180

    val off = (new Vector2(sign * -0.6f, 0)).rotate(angle)
    off.add(pos)

    val part = new BodyPart(isGrandma, Player.objectAtlas, "objects-01", world, off, new Vector2(1.0f, 0.35f), new Vector2(0, 0), 20, 0.1f, true, true)

    body.addBodyPart("item", part)
    body.connect("item", "leftLowerArm", "item", off)

    part
  }

  def releaseItem(items: ArrayBuffer[BodyPart]) : Unit = {
    body.removeJoint("item")
    holding = false
    prepThrow = false

    val item = body.removeBodyPart("item")
    items += item
  }

  def update(items: ArrayBuffer[BodyPart]) : Unit = {
    body.setAngleTarget("neck", 0, 100.0f)
    body.setAngleTarget("ground", 0, 100.0f)
    body.setAngleTarget("leftElbow", sign * Math.PI.toFloat / 6)

    body.setAngleTarget("rightElbow", sign * Math.PI.toFloat / 3)
    body.setAngleTarget("cane", 0, 20.0f)

    val moveCane = if (playerNumber == 1) {
      Gdx.input.isKeyPressed(Keys.W)
    } else {
      Gdx.input.isKeyPressed(Keys.UP)
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
        holding = true
        grabItem()
      }
      body.setAngleTarget("leftShoulder", sign * Math.PI.toFloat / 6)
      body.setAngleTarget("leftElbow", sign * -Math.PI.toFloat / 4)
      body.setAngleTarget("leftHip", sign * Math.PI.toFloat / 6, 30.0f)
      body.setAngleTarget("rightHip", sign * -Math.PI.toFloat / 3, 3.0f)

      body.setAngleTarget("rightShoulder", sign * Math.PI.toFloat / 3)

    } else if (leanForward) {
      if (holding) {
        prepThrow = true
      }
      body.setAngleTarget("leftShoulder", sign * Math.PI.toFloat, 8.0f, 4.0f)
      body.setAngleTarget("leftElbow", 0)
      body.setAngleTarget("leftHip", sign * -Math.PI.toFloat / 6, 30.0f)
      body.setAngleTarget("rightHip", sign * -Math.PI.toFloat / 6, 50.0f)

      body.setAngleTarget("rightShoulder", sign * Math.PI.toFloat / 3, 20.0f)

    } else {
      body.relaxJoint("leftShoulder")
      body.relaxJoint("rightHip")
      body.relaxJoint("rightShoulder")
      body.setAngleTarget("leftHip", 0, 30.0f)
    }

    if (moveCane) {
      body.setAngleTarget("rightShoulder", sign * -Math.PI.toFloat / 3, 10.0f)
      body.setAngleTarget("cane", sign * -Math.PI.toFloat / 3, 2.0f)
    }

    if (holding) {
      val lowerArm = body.getBodyPart("leftLowerArm")
      val angle = lowerArm.body.getAngle()
      body.setAngleTarget("item", angle + Math.PI.toFloat / 2, 10.0f, 1.0f)
    }
    if (prepThrow && !leanForward) {
      releaseItem(items)
    }

  }

  def render(batch: SpriteBatch) : Unit = {
    body.render(batch)
  }


}
