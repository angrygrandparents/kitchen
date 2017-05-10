package game.physics

import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.physics.box2d.BodyDef._
import com.badlogic.gdx.physics.box2d.joints._
import com.badlogic.gdx.math._
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.SpriteBatch


import game.Input


class Player(world: World, playerNumber: Int) {

  lazy val atlas = new TextureAtlas(Gdx.files.internal("grandma.atlas"));

  val body = {
    val body = new CharacterBody(world)

    val torso = new BodyPart(atlas, ("body"), world, new Vector2(0, 3.5f), new Vector2(0.8f, 1.0f), new Vector2(0, 0.3f))

    val head = new BodyPart(atlas, ("head"), world, new Vector2(0.1f, 4.9f), new Vector2(0.7f, 0.5f), new Vector2(-0.1f, 0.4f))

    val leftUpperArm = new BodyPart(atlas, ("UpperArm-L"), world, new Vector2(-1.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), 10)
    val leftLowerArm = new BodyPart(atlas, ("LowerArm-L"), world, new Vector2(-2.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), 10)

    val rightUpperArm = new BodyPart(atlas, ("UpperArm-R"), world, new Vector2(1.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), -10)
    val rightLowerArm = new BodyPart(atlas, ("LowerArm-R"), world, new Vector2(2.25f, 4.2f), new Vector2(0.5f, 0.1f), new Vector2(0, 0), -10)

    val leftLeg = new BodyPart(atlas, ("leg_L"), world, new Vector2(-0.6f, 2.0f), new Vector2(0.1f, 0.8f), new Vector2(0, 0), -10)
    val rightLeg = new BodyPart(atlas, ("leg_R"), world, new Vector2(0.6f, 2.0f), new Vector2(0.1f, 0.8f), new Vector2(0, 0), -10)

    body.addBodyPart("torso", torso)

    body.addBodyPart("head", head)

    body.addBodyPart("leftUpperArm", leftUpperArm)
    body.addBodyPart("leftLowerArm", leftLowerArm)

    body.addBodyPart("rightUpperArm", rightUpperArm)
    body.addBodyPart("rightLowerArm", rightLowerArm)

    body.addBodyPart("leftLeg", leftLeg)
    body.addBodyPart("rightLeg", rightLeg)

    body.connect("leftShoulder", "leftUpperArm", "torso", new Vector2(-0.7f, 4.2f))
    body.connect("leftElbow", "leftUpperArm", "leftLowerArm", new Vector2(-1.7f, 4.2f))

    body.connect("rightShoulder", "rightUpperArm", "torso", new Vector2(0.7f, 4.2f))
    body.connect("rightElbow", "rightUpperArm", "rightLowerArm", new Vector2(1.7f, 4.2f))

    body.connect("leftHip", "leftLeg", "torso", new Vector2(-0.6f, 2.7f))
    body.connect("rightHip", "rightLeg", "torso", new Vector2(0.6f, 2.7f))

    body.connect("neck", "head", "torso", new Vector2(0, 4.5f))

    body.translate(new Vector2(-3, -1))

    body
  }

  def update() : Unit = {

    var controlDir = new Vector2(0, 0)

    if (Gdx.input.isKeyPressed(Keys.W)) {
      controlDir.y += 1
    }
    if (Gdx.input.isKeyPressed(Keys.D)) {
      controlDir.x += 1
    }
    if (Gdx.input.isKeyPressed(Keys.S)) {
      controlDir.y -= 1
    }
    if (Gdx.input.isKeyPressed(Keys.A)) {
      controlDir.x -= 1
    }


  }

  def render(batch: SpriteBatch) : Unit = {
    body.render(batch)
  }


}
