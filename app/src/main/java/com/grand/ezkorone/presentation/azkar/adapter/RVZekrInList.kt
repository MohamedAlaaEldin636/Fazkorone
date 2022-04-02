package com.grand.ezkorone.presentation.azkar.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.search.ItemSearchQuery
import com.grand.ezkorone.presentation.search.adapter.viewModel.VHSearchQueries

class RVZekrInList : PagingDataAdapter<ItemZekrInList, VHZekrInList>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemZekrInList>() {
            override fun areItemsTheSame(
                oldItem: ItemZekrInList,
                newItem: ItemZekrInList
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemZekrInList,
                newItem: ItemZekrInList
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHZekrInList {
        return VHZekrInList(parent)
    }

    override fun onBindViewHolder(holder: VHZekrInList, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
