package com.grand.ezkorone.presentation.location.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder.VHItemTextInCard
import com.grand.ezkorone.presentation.location.adapter.viewModel.VHItemText

class RVItemText : ListAdapter<String, VHItemText>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemText {
        return VHItemText(parent)
    }

    override fun onBindViewHolder(holder: VHItemText, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
