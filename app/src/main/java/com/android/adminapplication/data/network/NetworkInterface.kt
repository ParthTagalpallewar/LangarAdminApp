package com.android.adminapplication.data.network

import com.android.adminapplication.data.models.SendOtpResponse
import com.android.adminapplication.data.models.Volentiere
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface NetworkInterface {
    @FormUrlEncoded
    @POST("sendSMS")
    suspend fun sendOtp(
            @Field("phone") phone: String,
    ): Response<SendOtpResponse>


    @FormUrlEncoded
    @POST("verifyCode")
    suspend fun verifyOtp(
            @Field("phone") phone: String,
            @Field("otp") otp: String,
            @Field("token") token: String?,
            @Field("type") type: String
    ): Response<Volentiere>






    companion object {
        operator fun invoke(
            //  networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : NetworkInterface {

          //  val BASE_URL = "http://192.168.43.146/Learing/send_otp/Welcome/"
            val BASE_URL = "https://digitdeveloper.online/langar/tesing/send_otp/welcome/"


            val loginInspector = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(loginInspector)
                //   .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                    //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NetworkInterface::class.java)
        }
    }
}