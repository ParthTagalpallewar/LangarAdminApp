package com.android.adminapplication.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.adminapplication.R
import com.android.adminapplication.data.repository.UserRepository
import com.android.adminapplication.databinding.FragmentVerifyOtpBinding
import com.android.adminapplication.ui.main.MainActivity
import com.android.adminapplication.utils.ApiException
import com.android.adminapplication.utils.move
import com.android.adminapplication.utils.snackBar
import com.android.adminapplication.utils.visible
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerifyOtpFragment : Fragment(R.layout.fragment_verify_otp) {

    private val binding: FragmentVerifyOtpBinding by viewBinding()
    var fcmToken: String? = null


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = arguments?.getString("argumentPhoneNumber")

        binding.tvEnterOtp.text = "Enter one time password send to \n$phoneNumber"

        getFirebaseToken()

        binding.btnResendCode.setOnClickListener {
            resendVerificationCode(phoneNumber)
        }

        binding.enterManually.setOnClickListener {
            pinCodeManually(phoneNumber)
        }


        binding.btnVerifyCode.setOnClickListener {
            verifyOtp(phoneNumber)
        }

    }

    private fun verifyOtp(phoneNumber: String?) {
        val otp = binding.otpView.otp

        if (otp.isNullOrBlank()) {
            binding.root.snackBar("Could not find otp")
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    binding.progressBar.visible(true)

                    val verifyUserResponse = UserRepository(requireContext()).verifyOtp(phoneNumber!!, otp, fcmToken)
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

    private fun resendVerificationCode(phoneNumber: String?) {

        binding.otpView.setOTP("")

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

    private fun pinCodeManually(phoneNumber: String?) {
        val fragment = VerifyOtpManually()
        val bundle = Bundle()
        bundle.putString("argumentPhoneNumber", phoneNumber)


        fragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.splash_container, fragment
        ).addToBackStack(null).commit()
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                fcmToken = it.result
            }
        }
    }

}