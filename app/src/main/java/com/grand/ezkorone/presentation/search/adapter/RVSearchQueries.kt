package com.grand.ezkorone.presentation.search.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.grand.ezkorone.data.favorite.repository.RepositoryFavorite
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.search.ItemSearchQuery
import com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder.VHItemHomeZekr
import com.grand.ezkorone.presentation.search.adapter.viewModel.VHSearchQueries

class RVSearchQueries : PagingDataAdapter<ItemSearchQuery, VHSearchQueries>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemSearchQuery>() {
            override fun areItemsTheSame(
                oldItem: ItemSearchQuery,
                newItem: ItemSearchQuery
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemSearchQuery,
                newItem: ItemSearchQuery
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHSearchQueries {
        return VHSearchQueries(parent)
    }

    override fun onBindViewHolder(holder: VHSearchQueries, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
