package com.android.adminapplication.data.network

import com.android.adminapplication.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }else{

            var errorMessage = ""

            try {
                val error = response.errorBody()?.string()
                val jsonObject = JSONObject(error!!)
                val errorStr = jsonObject.get("error")

                errorMessage = errorStr.toString()
            } catch (e: JSONException) {
                // Log.e("xyz$TAG", e.toString())
            }
            throw ApiException(errorMessage)
        }
    }

}