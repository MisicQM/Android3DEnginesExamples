package com.natasa

import android.app.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidGraphics
import com.natasa.libgdxexample.LibGDX2DTest

class ExamplesApp : Application() {

    override fun onCreate() {
        super.onCreate()
       // val config = AndroidApplicationConfiguration()
       // Gdx.app = AndroidGraphics(this, config, config.resolutionStrategy)
       // Gdx.app.applicationListener = LibGDX2DTest()

    }
}