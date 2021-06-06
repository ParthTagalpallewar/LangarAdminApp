package com.android.adminapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MainApplication: Application(){
    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate()


    }

}
