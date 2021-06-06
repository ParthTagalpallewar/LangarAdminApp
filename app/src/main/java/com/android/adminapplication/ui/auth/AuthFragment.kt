package com.android.adminapplication.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.adminapplication.R
import com.android.adminapplication.data.repository.UserRepository
import com.android.adminapplication.databinding.FragmentAuthBinding
import com.android.adminapplication.utils.ApiException
import com.android.adminapplication.utils.snackBar
import com.android.adminapplication.utils.visible
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "AuthFragment"

class AuthFragment : Fragment(R.layout.fragment_auth){

    val CREDENTIAL_PICKER_REQUEST = 1  // Set to an unused request code

    val binding: FragmentAuthBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showHint2()

        binding.ccpCountryCode.setDefaultCountryUsingNameCode("+91")

        binding.verifyPhone.setOnClickListener {

            val phoneNumberOnEditTextView = binding.mobileNumber.text.toString()

            if (phoneNumberOnEditTextView.isNullOrBlank()) {
                binding.root.snackBar("Please Enter or Choose Mobile Number", "OK") {
                    showHint2()
                }
            } else {

                var finalUserPhone: String? = null

                 if (phoneNumberOnEditTextView[0] == '+') {
                   finalUserPhone =  phoneNumberOnEditTextView
                } else {
                    val code = binding.ccpCountryCode.selectedCountryCode
                    val phoneWithCountry:String = "+" + code + phoneNumberOnEditTextView
                    finalUserPhone = phoneWithCountry
                }

                Log.e(TAG, "onViewCreated: phone form auth to text ${finalUserPhone.toString()}")

                val userRepo = UserRepository(requireContext())
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        binding.progressBar.visible()
                        val sendOtpResponse =  userRepo.sendSmsToNumber(finalUserPhone)
                        if(sendOtpResponse.result == "success"){
                            withContext(Dispatchers.Main){

                                delay(1000 * 4)
                                val fragment = VerifyOtpFragment()
                                val bundle = Bundle()
                                bundle.putString("argumentPhoneNumber", finalUserPhone)


                                fragment.arguments = bundle

                                 requireActivity().supportFragmentManager.beginTransaction().replace(
                                     R.id.splash_container, fragment
                                 ).commit()
                            binding.progressBar.visible(false);
                            }
                        }
                    } catch (e: Exception) {
                        binding.progressBar.visible(false);
                        if (e is ApiException){
                            binding.root.snackBar(e.message.toString())
                        }
                    }
                }
            }
        }
    }


    private fun showHint2() {

        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val credentialsClient = Credentials.getClient(requireContext())

        val intent = credentialsClient.getHintPickerIntent(hintRequest)

        startIntentSenderForResult(
            intent.intentSender,
            CREDENTIAL_PICKER_REQUEST,
            null,
            0,
            0,
            0,
            null
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            var credential: Credential? = data!!.getParcelableExtra(Credential.EXTRA_KEY)
            Log.e(TAG, "onActivityResult: ${credential?.id}")

            if (credential != null) {
                binding.mobileNumber.setText(credential.id)
            }


        }
    }


}