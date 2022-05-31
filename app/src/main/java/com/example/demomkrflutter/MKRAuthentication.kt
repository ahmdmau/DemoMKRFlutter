package com.example.demomkrflutter

import android.app.Activity
import android.content.Context
import android.content.Intent

const val URL_PARAMS_KEY = "urlParams"
const val IS_PRODUCTION_KEY = "isProd"
const val AUTH_TYPE_KEY = "authConfiguration"
const val REFRESH_TOKEN_KEY = "refreshToken"
const val CLIENT_ID = "clientId"
const val REQUEST_CODE_AUTH = 0
const val REQUEST_CODE_REFRESH = 1

class MKRAuthentication {

    companion object {
        private var clientId: String = ""
        private var isProduction: Boolean = false

        fun configure(clientId: String, config: MKRAuthenticationConfig) {
            this.clientId = clientId
            isProduction = config == MKRAuthenticationConfig.PRODUCTION
        }

        fun auth(activity: Activity) {
            val intent = Intent(activity, MKRAuthenticationActivity::class.java)
            intent.putExtra(URL_PARAMS_KEY, "scope=sso:profile&client_id=$clientId")
            intent.putExtra(IS_PRODUCTION_KEY, isProduction)
            intent.putExtra(AUTH_TYPE_KEY, MKRAuthType.AUTH.name)
            activity.startActivityForResult(intent, REQUEST_CODE_AUTH)
        }

        fun refresh(activity: Activity, accessToken: String) {
            val intent = Intent(activity, MKRAuthentication::class.java)
            intent.putExtra(REFRESH_TOKEN_KEY, accessToken)
            intent.putExtra(IS_PRODUCTION_KEY, isProduction)
            intent.putExtra(CLIENT_ID, clientId)
            intent.putExtra(AUTH_TYPE_KEY, MKRAuthType.REFRESH.name)
            activity.startActivityForResult(intent, REQUEST_CODE_REFRESH)
        }

    }
}