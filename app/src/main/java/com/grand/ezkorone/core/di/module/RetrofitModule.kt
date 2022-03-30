package com.grand.ezkorone.core.di.module

import com.google.gson.Gson
import com.grand.ezkorone.data.azan.dataSource.remote.ApiAzanServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    const val BASE_URL = "https://fazkrony.my-staff.net/api/"

    private const val AZAN_BASE_URL = "https://api.aladhan.com/v1/"

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideApiAzanServices(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
    ): ApiAzanServices {
        return Retrofit.Builder()
            .baseUrl(AZAN_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create()
    }

}
