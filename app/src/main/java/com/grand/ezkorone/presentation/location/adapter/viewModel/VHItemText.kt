package com.grand.ezkorone.presentation.location.adapter.viewModel

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.fromJsonOrNull
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.core.extensions.toJson
import com.grand.ezkorone.databinding.ItemTextBinding
import com.grand.ezkorone.databinding.ItemTextInCardBinding
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections

class VHItemText(
    parent: ViewGroup,
    private val gson: Gson,
    private val onSelection: (View, LocationData) -> Unit,
) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_text, parent)
) {

    private val binding = ItemTextBinding.bind(itemView)

    init {
        binding.textView.setOnClickListener {
            val locationData = (binding.textView.tag as? String)
                .fromJsonOrNull<LocationData>(gson) ?: return@setOnClickListener

            onSelection(it, locationData)
        }
    }

    fun bind(item: LocationData) {
        binding.textView.tag = item.toJson(gson)

        binding.textView.text = item.address
    }

}
