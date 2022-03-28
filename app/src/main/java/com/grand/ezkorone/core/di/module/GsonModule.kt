package com.grand.ezkorone.core.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GsonModule {
	
	@Provides
	@Singleton
	fun provideGson(): Gson {
		return GsonBuilder()
			.disableHtmlEscaping()
			.setLenient()
			.serializeNulls()
			.create()
	}
	
}