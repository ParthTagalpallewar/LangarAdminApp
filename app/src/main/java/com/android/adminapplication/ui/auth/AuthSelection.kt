package com.android.adminapplication.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.adminapplication.R
import com.android.adminapplication.databinding.ActivityAuthSelectionBinding
import com.android.adminapplication.databinding.ActivitySplashScreenBinding

class AuthSelection : AppCompatActivity() {

    private lateinit var binding: ActivityAuthSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvRegister.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.authContainer,AuthFragment()).commit()
        }



    }
}