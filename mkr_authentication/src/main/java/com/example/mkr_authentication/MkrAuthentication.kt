package com.example.mkr_authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

const val URL_PARAMS_KEY = "urlParams"
const val IS_PRODUCTION_KEY = "isProd"
const val REFRESH_TOKEN_KEY = "refreshToken"
const val REQUEST_CODE_AUTH = 1122

class MKRAuthentication {

    companion object {
        private var clientId: String = ""
        private var isProduction: Boolean = false
        private lateinit var flutterEngine: FlutterEngine
        const val ENGINE_ID = "auth_engine"

        fun configure(clientId: String, config: MkrAuthenticationConfig, context: Context) {
            Companion.clientId = clientId
            isProduction = config == MkrAuthenticationConfig.PRODUCTION

            flutterEngine = FlutterEngine(context).apply {
                dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
            }
            FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine)
        }

        fun auth(activity: Activity) {
            val intent = Intent(activity, MKRAuthenticationActivity::class.java)
            intent.putExtra(REFRESH_TOKEN_KEY, "")
            intent.putExtra(URL_PARAMS_KEY, "scope=sso:profile&client_id=$clientId")
            intent.putExtra(IS_PRODUCTION_KEY, isProduction)
            activity.startActivityForResult(intent, REQUEST_CODE_AUTH)
        }

        fun refresh(activity: Activity, refreshToken: String) {
            val intent = Intent(activity, MKRAuthentication::class.java)
            intent.putExtra(REFRESH_TOKEN_KEY, refreshToken)
            intent.putExtra(URL_PARAMS_KEY, "scope=sso:profile&client_id=$clientId")
            intent.putExtra(IS_PRODUCTION_KEY, isProduction)
            activity.startActivityForResult(intent, REQUEST_CODE_AUTH)
        }

    }
}