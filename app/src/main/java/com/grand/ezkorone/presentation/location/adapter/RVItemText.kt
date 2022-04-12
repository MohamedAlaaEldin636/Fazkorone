package com.grand.ezkorone.presentation.location.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.gson.Gson
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder.VHItemTextInCard
import com.grand.ezkorone.presentation.location.adapter.viewModel.VHItemText

class RVItemText(
    private val gson: Gson,
    private val onSelection: (view: View, locationData: LocationData) -> Unit,
) : ListAdapter<LocationData, VHItemText>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<LocationData>() {
            override fun areItemsTheSame(
                oldItem: LocationData,
                newItem: LocationData
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: LocationData,
                newItem: LocationData
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemText {
        return VHItemText(parent, gson, onSelection)
    }

    override fun onBindViewHolder(holder: VHItemText, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
