package com.sks225.snapeat

import android.app.Application
import com.sks225.snapeat.repository.AppContainer

class SnapEatApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        instance = this
        container = AppContainer()
    }

    companion object {
        lateinit var instance: SnapEatApplication
            private set
    }
}
