package com.grand.ezkorone.presentation.drawer.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.grand.ezkorone.R
import com.grand.ezkorone.data.favorite.repository.RepositoryFavorite
import com.grand.ezkorone.domain.favorite.ItemFavorite
import com.grand.ezkorone.domain.notifications.ItemNotification
import com.grand.ezkorone.presentation.drawer.adapters.viewHolder.VHItemFavZekrDetail
import com.grand.ezkorone.presentation.drawer.adapters.viewHolder.VHItemFavorite
import com.grand.ezkorone.presentation.drawer.adapters.viewHolder.VHItemHomeZekr2
import com.grand.ezkorone.presentation.drawer.adapters.viewHolder.VHItemNotification

class RVItemFavorite(val repoFavorite: RepositoryFavorite) : PagingDataAdapter<ItemFavorite, VHItemFavorite>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemFavorite>() {
            override fun areItemsTheSame(
                oldItem: ItemFavorite,
                newItem: ItemFavorite
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemFavorite,
                newItem: ItemFavorite
            ): Boolean = oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.type == 1) {
            R.layout.item_fav_zekr_detail
        }else {
            R.layout.item_home_zekr
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemFavorite {
        return if (viewType == R.layout.item_fav_zekr_detail) {
            VHItemFavZekrDetail(parent, this)
        }else {
            VHItemHomeZekr2(parent, this)
        }
    }

    override fun onBindViewHolder(holder: VHItemFavorite, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}
