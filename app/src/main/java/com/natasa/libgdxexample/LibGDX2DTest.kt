package com.natasa.libgdxexample
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World


import com.badlogic.gdx.utils.JsonReader

class LibGDX2DTest : ApplicationAdapter() {
    private lateinit var camera: OrthographicCamera
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var texture: Texture
    private lateinit var world: World
    private lateinit var body: Body
    private val pixelsPerMeter = 100f // Adjust this scale as needed

    override fun create() {
        camera = OrthographicCamera(500f, 500f).apply {
            setToOrtho(false)
        }
        spriteBatch = SpriteBatch()

        texture = Texture("textures/defaultCar.png")
        world = World(Vector2(0f, -10f), true)

        createPhysicsBody()
    }
    private fun createCarModel() {
        val modelBuilder = ModelBuilder()
        val attributes = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()
        val material = Material(ColorAttribute.createDiffuse(Color.RED))
       // carModel = G3dModelLoader(JsonReader()).loadModel(Gdx.files.internal("model.g3dj"))
    }
    private fun createPhysicsBody() {
        val bodyDef = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            position.set((Gdx.graphics.width / 2) / pixelsPerMeter, (Gdx.graphics.height / 2) / pixelsPerMeter)
        }
        body = world.createBody(bodyDef)

        val shape = PolygonShape().apply {
            setAsBox((texture.width / 2) / pixelsPerMeter, (texture.height / 2) / pixelsPerMeter)
        }
        val fixtureDef = FixtureDef().apply {
            this.shape = shape
            density = 1f
            friction = 0.4f
            restitution = 0.5f
        }
        body.createFixture(fixtureDef)
        shape.dispose()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        spriteBatch.projectionMatrix = camera.combined

        world.step(1/60f, 6, 2)

        spriteBatch.begin()
        spriteBatch.draw(texture, body.position.x * pixelsPerMeter - texture.width / 2, body.position.y * pixelsPerMeter - texture.height / 2)
        spriteBatch.end()
    }

    override fun dispose() {
        texture.dispose()
        world.dispose()
        spriteBatch.dispose()
    }
}