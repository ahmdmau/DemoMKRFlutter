package com.example.demomkrflutter

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MainApplication: Application() {

    companion object {
        const val ENGINE_ID = "auth_engine"
    }

    private lateinit var flutterEngine: FlutterEngine

    override fun onCreate() {
        super.onCreate()
        flutterEngine = FlutterEngine(this).apply {
            dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        }
        FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine)
    }
}