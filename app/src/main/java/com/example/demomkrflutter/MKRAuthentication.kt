package com.example.demomkrflutter

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.demomkrflutter.MainApplication.Companion.ENGINE_ID
import io.flutter.embedding.android.FlutterActivity

class MKRAuthentication {
    companion object {
        fun auth(context: Context) {
            val arguments = HashMap<String, Any>()
            arguments.put("urlParams", "scope=sso:profile&client_id=MEKARI_FLEX_CLIENT_ID")
            arguments.put("isProd", false)
            val intent = Intent(context, MKRAuthenticationActivity::class.java)
            intent.putExtra("urlParams", "scope=sso:profile&client_id=MEKARI_FLEX_CLIENT_ID")
            intent.putExtra("isProd", false)
            context.startActivity(intent)
        }
    }

    class CachedEngineAuthIntentBuilder(engineId: String): FlutterActivity.CachedEngineIntentBuilder(MKRAuthenticationActivity::class.java, engineId) { }
}