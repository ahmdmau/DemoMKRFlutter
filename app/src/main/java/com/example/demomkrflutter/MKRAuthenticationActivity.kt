package com.example.demomkrflutter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.demomkrflutter.MainApplication.Companion.ENGINE_ID
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel

const val methodChannel = "com.mekari.auth_module"
const val authResponse = "authResponse"
const val refreshTokenResponse = "refreshResponse"

class MKRAuthenticationActivity : FlutterActivity() {

    private val channel = FlutterEngineCache.getInstance().get(ENGINE_ID)?.dartExecutor?.let {
        MethodChannel(
            it.binaryMessenger, methodChannel)
    }

    override fun onResume() {
        super.onResume()
        val authType = intent.getStringExtra(AUTH_TYPE_KEY)
        authType?.let { initializeMethod(it) }
    }

    private fun initializeMethod(authType: String) {
        if (authType == MKRAuthType.AUTH.name) {
            val authArguments = HashMap<String, Any>()
            intent.getStringExtra(URL_PARAMS_KEY)?.let { authArguments.put(URL_PARAMS_KEY, it) }
            authArguments.put(IS_PRODUCTION_KEY, intent.getBooleanExtra(IS_PRODUCTION_KEY, false))

            channel!!.invokeMethod(MKRAuthType.AUTH.name.lowercase(), authArguments)
        } else {
            val refreshArguments = HashMap<String, Any>()
            intent.getStringExtra(REFRESH_TOKEN_KEY)
                ?.let { refreshArguments.put(REFRESH_TOKEN_KEY, it) }
            intent.getStringExtra(CLIENT_ID)?.let { refreshArguments.put(CLIENT_ID, it) }
            refreshArguments.put(IS_PRODUCTION_KEY, intent.getBooleanExtra(IS_PRODUCTION_KEY, false))

            channel!!.invokeMethod(MKRAuthType.REFRESH.name.lowercase(), refreshArguments)
        }
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel!!.setMethodCallHandler { call, _ ->
            when (call.method) {
                authResponse -> {
                    val accessToken = call.argument<String>("access_token")
                    val tokenType = call.argument<String>("token_type")
                    val expiresIn = call.argument<Int>("expires_in")
                    val refreshToken = call.argument<String>("refresh_token")

                    val authResultMapped = HashMap<String, Any>()
                    accessToken?.let { authResultMapped.put("accessToken", it) }
                    tokenType?.let { authResultMapped.put("tokenType", it) }
                    expiresIn?.let { authResultMapped.put("expiresIn", it) }
                    refreshToken?.let { authResultMapped.put("refreshToken", it) }

                    val intent = Intent().apply {
                        putExtra(authResponse, authResultMapped)
                    }
                    setResult(REQUEST_CODE_AUTH, intent)
                    finish()
                }
                refreshTokenResponse -> {
                    val refreshTokenMessage = call.arguments as String
                    val intent = Intent().apply {
                        putExtra(refreshTokenResponse, refreshTokenMessage)
                    }
                    setResult(REQUEST_CODE_REFRESH, intent)
                    finish()
                }
            }
        }
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get(ENGINE_ID)
    }
}