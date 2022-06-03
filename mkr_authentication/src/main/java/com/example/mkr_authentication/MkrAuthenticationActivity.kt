package com.example.mkr_authentication

import android.content.Context
import android.content.Intent
import com.example.mkr_authentication.MKRAuthentication.Companion.ENGINE_ID
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

const val methodChannel = "com.mekari.auth_module"
const val authResponse = "authResponse"
const val authFailedResponse = "authFailedResponse"

class MKRAuthenticationActivity : FlutterActivity() {
    private val channel = FlutterEngineCache.getInstance().get(ENGINE_ID)?.dartExecutor?.let {
        MethodChannel(
            it.binaryMessenger, methodChannel
        )
    }

    override fun onResume() {
        super.onResume()
        initializeMethod()
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel!!.setMethodCallHandler { call, _ ->
            when (call.method) {
                authResponse -> {
                    handleAuthResponse(call)
                }
                authFailedResponse -> {
                    handleAuthFailedResponse(call)
                }
            }
        }
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get(ENGINE_ID)
    }

    private fun handleAuthResponse(call: MethodCall) {
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

    private fun handleAuthFailedResponse(call: MethodCall) {
        val refreshTokenMessage = call.arguments as String
        val intent = Intent().apply {
            putExtra(authFailedResponse, refreshTokenMessage)
        }
        setResult(REQUEST_CODE_AUTH, intent)
        finish()
    }

    private fun initializeMethod() {
        val authArguments = HashMap<String, Any>()
        intent.getStringExtra(URL_PARAMS_KEY)?.let { authArguments.put(URL_PARAMS_KEY, it) }
        authArguments[IS_PRODUCTION_KEY] = intent.getBooleanExtra(IS_PRODUCTION_KEY, false)
        intent.getStringExtra(REFRESH_TOKEN_KEY)
            ?.let { authArguments.put(REFRESH_TOKEN_KEY, it) }
        channel!!.invokeMethod("auth", authArguments)
    }

}