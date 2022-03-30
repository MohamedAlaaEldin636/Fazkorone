package com.grand.ezkorone.presentation.internalNavigation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.grand.ezkorone.data.favorite.repository.RepositoryFavorite
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder.VHItemHomeZekr

class RVItemHomeZekr(val repoFavorite: RepositoryFavorite) : PagingDataAdapter<ItemZekrTopCategory, VHItemHomeZekr>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemZekrTopCategory>() {
            override fun areItemsTheSame(
                oldItem: ItemZekrTopCategory,
                newItem: ItemZekrTopCategory
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemZekrTopCategory,
                newItem: ItemZekrTopCategory
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemHomeZekr {
        return VHItemHomeZekr(parent, this)
    }

    override fun onBindViewHolder(holder: VHItemHomeZekr, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
