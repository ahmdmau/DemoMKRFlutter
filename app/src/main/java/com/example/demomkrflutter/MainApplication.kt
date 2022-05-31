package com.example.demomkrflutter

import android.app.Application
import com.example.mkr_authentication.MKRAuthentication
import com.example.mkr_authentication.MKRAuthenticationConfig

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MKRAuthentication.configure("YOUR_CLIENT_ID", MKRAuthenticationConfig.STAGING, this)
    }
}