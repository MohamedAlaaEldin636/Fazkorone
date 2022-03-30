package com.grand.ezkorone.presentation.internalNavigation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder.VHItemTextInCard

class RVItemTextInCard : ListAdapter<IdAndName, VHItemTextInCard>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<IdAndName>() {
            override fun areItemsTheSame(
                oldItem: IdAndName,
                newItem: IdAndName
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: IdAndName,
                newItem: IdAndName
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemTextInCard {
        return VHItemTextInCard(parent)
    }

    override fun onBindViewHolder(holder: VHItemTextInCard, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
