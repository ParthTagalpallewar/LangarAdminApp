package com.android.adminapplication.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.android.adminapplication.R
import com.android.adminapplication.data.repository.UserRepository
import com.android.adminapplication.databinding.ActivitySplashScreenBinding
import com.android.adminapplication.ui.auth.AuthFragment
import com.android.adminapplication.ui.main.MainActivity
import com.android.adminapplication.utils.move
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding


    //delay time in milli seconds
    private val delayTime = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        this.lifecycleScope.launchWhenStarted {

            //delay this time of given time
            delay(delayTime * 1000L)

            val userRepo = UserRepository(this@SplashScreen)



            if (userRepo.getUserData() == null) {

                supportFragmentManager.beginTransaction().add(R.id.splash_container, AuthFragment())
                    .commit()
            } else {
                //moving to main activity
                this@SplashScreen.move(MainActivity::class.java, true)
            }


        }
    }
}


