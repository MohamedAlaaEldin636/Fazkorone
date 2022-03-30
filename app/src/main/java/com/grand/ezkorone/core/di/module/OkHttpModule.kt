package com.grand.ezkorone.core.di.module

import android.content.Context
import android.os.Build
import com.grand.ezkorone.core.di.module.qualifiers.HeadersInterceptor
import com.grand.ezkorone.core.extensions.getDeviceIdWithoutPermission
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {
	
	private const val TIMEOUT_IN_SEC = 30L
	
	private const val HEADER_KEY_DEVICE = "device"
	private const val HEADER_KEY_PLATFORM = "platform"
	private const val HEADER_VALUE_PLATFORM = "1"

	@Singleton
	@Provides
	fun provideOkHttpClient(
		@HeadersInterceptor headersInterceptor: Interceptor,
		httpLoggingInterceptor: HttpLoggingInterceptor
	): OkHttpClient {
		return OkHttpClient.Builder().apply {
			connectTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
			readTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
			writeTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS)
			
			addInterceptor(headersInterceptor)
			
			addNetworkInterceptor(httpLoggingInterceptor)
		}.build()
	}
	
	@HeadersInterceptor
	@Provides
	fun provideHeadersInterceptor(
		@ApplicationContext context: Context
	): Interceptor {
		return Interceptor { chain ->
			val request = chain.request()

			val url = request.url.toString()

			val builder = request.newBuilder()

			if (url.startsWith(RetrofitModule.BASE_URL)) {
				builder.addHeader(HEADER_KEY_DEVICE, context.getDeviceIdWithoutPermission())
				builder.addHeader(HEADER_KEY_PLATFORM, HEADER_VALUE_PLATFORM)
			}

			chain.proceed(builder.build())
		}
	}
	
	/**
	 * - Used for debugging.
	 */
	@Provides
	fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor { message ->
			Timber.d(message)
		}.also {
			it.level = HttpLoggingInterceptor.Level.BODY
		}
	}
	
}
