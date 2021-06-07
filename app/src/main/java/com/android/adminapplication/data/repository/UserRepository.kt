package com.android.adminapplication.data.repository

import android.content.Context
import com.android.adminapplication.data.models.SendOtpResponse
import com.android.adminapplication.data.models.Volentiere
import com.android.adminapplication.data.network.NetworkConnectionInterceptor
import com.android.adminapplication.data.network.NetworkInterface
import com.android.adminapplication.data.network.SafeApiRequest
import com.android.adminapplication.data.roomdb.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(val context: Context) : SafeApiRequest() {

    lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    init {
        networkConnectionInterceptor = NetworkConnectionInterceptor(context)
    }

    suspend fun getUserData() = withContext(Dispatchers.IO) {
        return@withContext AppDatabase.invoke(context).getUserDao().getCurrentUser()
    }

    suspend fun addUser(volentiere : Volentiere) = withContext(Dispatchers.IO){
        return@withContext AppDatabase.invoke(context).getUserDao().insertUser(volentiere)
    }

    suspend fun sendSmsToNumber(phone:String): SendOtpResponse {

        return apiRequest { NetworkInterface.invoke().sendOtp(phone) }

    }

    suspend fun verifyOtp(phone: String, otp: String, fcmToken: String?): Volentiere {

        return apiRequest { NetworkInterface.invoke().verifyOtp(phone,otp,fcmToken) }

    }


}