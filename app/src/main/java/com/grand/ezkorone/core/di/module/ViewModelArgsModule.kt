package com.grand.ezkorone.core.di.module

import androidx.lifecycle.SavedStateHandle
import com.grand.ezkorone.core.extensions.asBundle
import com.grand.ezkorone.presentation.azkar.AzkarListFragmentArgs
import com.grand.ezkorone.presentation.drawer.dialogs.AlarmTimePickerDialogFragmentArgs
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentArgs
import com.grand.ezkorone.presentation.sheikh.SheikhListFragmentArgs
import com.grand.ezkorone.presentation.taspeh.TaspehListFragmentArgs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

    @Provides
    fun provideLocationSelectionFragmentArgs(state: SavedStateHandle): LocationSelectionFragmentArgs {
        return LocationSelectionFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideAzkarListFragmentArgs(state: SavedStateHandle): AzkarListFragmentArgs {
        return AzkarListFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideSheikhListFragmentArgs(state: SavedStateHandle): SheikhListFragmentArgs {
        return SheikhListFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideAlarmTimePickerDialogFragmentArgs(state: SavedStateHandle): AlarmTimePickerDialogFragmentArgs {
        return AlarmTimePickerDialogFragmentArgs.fromBundle(state.asBundle())
    }

    @Provides
    fun provideTaspehListFragmentArgs(state: SavedStateHandle): TaspehListFragmentArgs {
        return TaspehListFragmentArgs.fromBundle(state.asBundle())
    }

}
