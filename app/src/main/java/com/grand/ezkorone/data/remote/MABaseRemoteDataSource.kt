package com.grand.ezkorone.data.remote

import com.google.gson.Gson
import com.grand.ezkorone.core.extensions.fromJson
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MAResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

open class MABaseRemoteDataSource {
	
	protected suspend fun <T> safeApiCall(apiCall: suspend () -> MABaseResponse<T>): MAResult.Immediate<MABaseResponse<T>> = withContext(Dispatchers.IO) {
		try {
			Timber.d("abc hiiiiiiiiiiiii")
			
			val response = apiCall()
			
			Timber.d("response $response")
			
			val errorStatus = when (response.code) {
				200 -> {
					return@withContext MAResult.Success(response)
				}
				/*403 -> {
					MAResult.Failure.Status.TOKEN_EXPIRED
				}
				405 -> {
					// Not used in this project as always on login you should re-verify, and in
					// case of social login check if has phone number if so then just ignore
					// verification step
					MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED
				}*/
				401 -> {
					MAResult.Failure.Status.ERROR
				}
				in 500 until 600 -> {
					MAResult.Failure.Status.SERVER_ERROR
				}
				else -> {
					MAResult.Failure.Status.OTHER
				}
			}
			
			MAResult.Failure(errorStatus, response.code, response.message)
		}catch (throwable: Throwable) {
			Timber.d("safeApiCall throwable $throwable")
			
			when (throwable) {
				is HttpException -> {
					Timber.d("code ${throwable.code()}, msg ${throwable.message}, http status msg ${throwable.message()}")
					
					val errorStatus = when (throwable.code()) {
						in 400 until 500 -> MAResult.Failure.Status.ERROR
						in 500 until 600 -> MAResult.Failure.Status.SERVER_ERROR
						else -> MAResult.Failure.Status.OTHER
					}
					
					MAResult.Failure(errorStatus, throwable.code(), throwable.message())
				}
				is UnknownHostException/*, is SocketException*/, is ConnectException -> {
					MAResult.Failure(MAResult.Failure.Status.NO_INTERNET, message = throwable.message)
				}
				else -> {
					MAResult.Failure(MAResult.Failure.Status.OTHER, message = throwable.message)
				}
			}
		}
	}
	
	/*@Suppress("UNCHECKED_CAST")
	suspend inline fun <reified T> safeApiCall2(gson: Gson, crossinline apiCall: suspend () -> ResponseBody): T? = withContext(Dispatchers.IO) {
		try {
			Timber.d("abc hiiiiiiiiiiiii")

			val response = apiCall()
			
			Timber.d("response $response")
			
			val string = kotlin.runCatching {
				response.string()
			}.getOrNull()
			
			if (string.isNullOrEmpty()) {
				null
			}else {
				val jsonObject = JSONObject()
				for (pair in string.split("&")) {
					val (key, value) = pair.split("=").let { it[0] to it[1] }
					
					jsonObject.put(key, value)
				}
				
				jsonObject.toString().fromJson<T>(gson)
			}
			
			*//*val errorStatus = when (response.code()) {
				200 -> {
					return@withContext MAResult.Success(response.body() as T)
				}
				403 -> {
					MAResult.Failure.Status.TOKEN_EXPIRED
				}
				405 -> {
					// Not used in this project as always on login you should re-verify, and in
					// case of social login check if has phone number if so then just ignore
					// verification step
					MAResult.Failure.Status.ACTIVATION_NOT_VERIFIED
				}
				401 -> {
					MAResult.Failure.Status.ERROR
				}
				in 500 until 600 -> {
					MAResult.Failure.Status.SERVER_ERROR
				}
				else -> {
					MAResult.Failure.Status.OTHER
				}
			}*//*
			
			//MAResult.Failure(errorStatus, response.code(), response.message())
			
			//response.body()
		}catch (throwable: Throwable) {
			Timber.d("safeApiCall throwable $throwable")
			
			*//*when (throwable) {
				is HttpException -> {
					Timber.d("code ${throwable.code()}, msg ${throwable.message}, http status msg ${throwable.message()}")
					
					val errorStatus = when (throwable.code()) {
						in 400 until 500 -> MAResult.Failure.Status.ERROR
						in 500 until 600 -> MAResult.Failure.Status.SERVER_ERROR
						else -> MAResult.Failure.Status.OTHER
					}
					
					MAResult.Failure(errorStatus, throwable.code(), throwable.message())
				}
				is UnknownHostException*//**//*, is SocketException*//**//*, is ConnectException -> {
					MAResult.Failure(MAResult.Failure.Status.NO_INTERNET)
				}
				else -> {
					MAResult.Failure(MAResult.Failure.Status.OTHER)
				}
			}*//*
			
			null
		}
	}*/
	
}
