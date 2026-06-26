package com.example.yazumi

import android.app.Application
import com.example.yazumi.data.AppContainer

class YazumiApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
