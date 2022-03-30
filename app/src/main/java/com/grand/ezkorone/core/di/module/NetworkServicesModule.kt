package com.grand.ezkorone.core.di.module

import com.grand.ezkorone.data.favorite.dataSource.remote.ApiFavoriteServices
import com.grand.ezkorone.data.home.dataSource.remote.ApiHomeServices
import com.grand.ezkorone.data.notifications.dataSource.remote.ApiNotificationsServices
import com.grand.ezkorone.data.settings.dataSource.remote.ApiSettingsServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkServicesModule {

    @Provides
    @Singleton
    fun provideApiSettingsServices(retrofit: Retrofit): ApiSettingsServices =
        retrofit.create(ApiSettingsServices::class.java)

    @Provides
    @Singleton
    fun provideApiNotificationsServices(retrofit: Retrofit): ApiNotificationsServices =
        retrofit.create(ApiNotificationsServices::class.java)

    @Provides
    @Singleton
    fun provideApiHomeServices(retrofit: Retrofit): ApiHomeServices =
        retrofit.create(ApiHomeServices::class.java)

    @Provides
    @Singleton
    fun provideApiFavoriteServices(retrofit: Retrofit): ApiFavoriteServices =
        retrofit.create(ApiFavoriteServices::class.java)

}
