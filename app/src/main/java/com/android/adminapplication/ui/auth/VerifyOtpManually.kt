package com.android.adminapplication.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.adminapplication.R
import com.android.adminapplication.data.repository.UserRepository
import com.android.adminapplication.databinding.FragmentVerifyOtpManuallyBinding
import com.android.adminapplication.ui.main.MainActivity
import com.android.adminapplication.utils.ApiException
import com.android.adminapplication.utils.move
import com.android.adminapplication.utils.snackBar
import com.android.adminapplication.utils.visible
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerifyOtpManually : Fragment(R.layout.fragment_verify_otp_manually) {

    val binding: FragmentVerifyOtpManuallyBinding by viewBinding()
    var fcmToken:String? = null


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = arguments?.getString("argumentPhoneNumber")

        getFirebaseToken()

        binding.tvEnterOtp.text = "Enter one time password send to \n$phoneNumber"

        binding.btnResendCode.setOnClickListener {
            resendCode(phoneNumber)
        }

        binding.btnVerifyCode.setOnClickListener {
            verifyCode(phoneNumber)
        }

    }

    private fun verifyCode(phoneNumber: String?) {
        val otp = binding.etOtp.text.toString()

        if (otp.isNullOrBlank()) {
            binding.root.snackBar("Could not find otp")
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    binding.progressBar.visible(true)
                    val verifyUserResponse =
                            UserRepository(requireContext()).verifyOtp(phoneNumber!!, otp, fcmToken)
                    if (verifyUserResponse.phone != null) {
                        UserRepository(requireContext()).addUser(verifyUserResponse)
                        requireContext().move(MainActivity::class.java, true)
                    }

                } catch (e: Exception) {
                    binding.progressBar.visible(false)

                    if (e is ApiException) {
                        binding.root.snackBar(e.message.toString())
                    }
                }
            }
        }
    }

    private fun resendCode(phoneNumber: String?) {
        binding.etOtp.setText("")
        val userRepo = UserRepository(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                binding.progressBar.visible()
                val sendOtpResponse = userRepo.sendSmsToNumber(phoneNumber!!)
                if (sendOtpResponse.result == "success") {
                    withContext(Dispatchers.Main) {
                        binding.root.snackBar(sendOtpResponse.message)
                        binding.progressBar.visible(false);
                    }
                }
            } catch (e: Exception) {
                binding.progressBar.visible(false);
                if (e is ApiException) {
                    binding.root.snackBar(e.message.toString())
                }
            }
        }
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful){
                fcmToken = it.result
            }
        }
    }

}