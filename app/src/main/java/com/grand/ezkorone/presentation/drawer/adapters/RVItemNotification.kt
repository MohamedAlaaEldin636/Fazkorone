package com.grand.ezkorone.presentation.drawer.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grand.ezkorone.domain.notifications.ItemNotification
import com.grand.ezkorone.presentation.drawer.adapters.viewHolder.VHItemNotification
import com.grand.ezkorone.presentation.location.adapter.viewModel.VHItemText

class RVItemNotification : PagingDataAdapter<ItemNotification, VHItemNotification>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemNotification>() {
            override fun areItemsTheSame(
                oldItem: ItemNotification,
                newItem: ItemNotification
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemNotification,
                newItem: ItemNotification
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemNotification {
        return VHItemNotification(parent)
    }

    override fun onBindViewHolder(holder: VHItemNotification, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
