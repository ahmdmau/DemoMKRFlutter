package com.example.demomkrflutter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demomkrflutter.MainApplication.Companion.ENGINE_ID
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel

class MKRAuthenticationActivity : FlutterActivity() {

    override fun onResume() {
        super.onResume()
        val channel = FlutterEngineCache.getInstance().get(ENGINE_ID)?.dartExecutor?.let {
            MethodChannel(
                it.binaryMessenger, "com.mekari.auth_module")
        }
        val arguments = HashMap<String, Any>()
        intent.getStringExtra("urlParams")?.let { arguments.put("urlParams", it) }
        arguments.put("isProd", intent.getBooleanExtra("isProd", false))

        channel!!.invokeMethod("auth", arguments)
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get(ENGINE_ID)
    }
}