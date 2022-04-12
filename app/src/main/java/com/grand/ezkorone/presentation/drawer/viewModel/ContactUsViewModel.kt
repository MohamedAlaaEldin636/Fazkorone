package com.grand.ezkorone.presentation.drawer.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.data.settings.repository.RepositorySettings
import com.grand.ezkorone.domain.contactUs.ItemContactUsPurpose
import com.grand.ezkorone.presentation.drawer.ContactUsFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(
    application: Application,
    private val repoSettings: RepositorySettings,
) : AndroidViewModel(application) {

    val name = MutableLiveData("")

    val email = MutableLiveData("")

    val purpose = MutableLiveData("")

    val message = MutableLiveData("")

    val imageUri = MutableLiveData<Uri>()

    var availablePurposes = emptyList<ItemContactUsPurpose>()

    val retryAbleFlowPurposes = RetryAbleFlow(repoSettings::getContactUsPurposes)

    private var selectedItem: ItemContactUsPurpose? = null

    fun showPurposeOfContactPopUp(view: View) {
        view.showPopup(availablePurposes.map { it.name }) { menuItem ->
            val item = availablePurposes.firstOrNull { it.name == menuItem.title }
                ?: return@showPopup

            selectedItem = item

            purpose.value = item.name
        }
    }

    fun pickImage(view: View) {
        view.findFragment<ContactUsFragment>().pickImageOrRequestPermissions()
    }

    fun send(view: View) {
        val purposeId = selectedItem?.id

        if (name.value.isNullOrEmpty() || email.value.isNullOrEmpty() || purposeId == null
            || purpose.value.isNullOrEmpty() || message.value.isNullOrEmpty()) {
            view.context.showErrorToast(view.context.getString(R.string.all_fields_required))

            return
        }

        val fragment = view.findFragment<ContactUsFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoSettings.contactUs(
                    name.value.orEmpty(),
                    email.value.orEmpty(),
                    purposeId,
                    message.value.orEmpty(),
                    imageUri.value?.createMultipartBodyPart(myApp, "image")
                )
            },
            afterHidingLoading = {
                view.context.showSuccessToast(view.context.getString(R.string.sent_successfully))

                view.post {
                    fragment.findNavController().navigateUp()
                }
            },
            canCancelDialog = true
        )
    }

}
