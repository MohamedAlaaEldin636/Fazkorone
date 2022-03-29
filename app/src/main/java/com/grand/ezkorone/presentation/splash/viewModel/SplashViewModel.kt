package com.grand.ezkorone.presentation.splash.viewModel

import androidx.lifecycle.ViewModel
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.data.settings.repository.RepositorySettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    repository: RepositorySettings
) : ViewModel() {

    val retryAbleFlow = RetryAbleFlow(repository::getInitialAppRequirementsAndSetFirebaseToken)

}