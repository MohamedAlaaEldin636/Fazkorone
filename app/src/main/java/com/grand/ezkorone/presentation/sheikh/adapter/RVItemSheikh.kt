package com.grand.ezkorone.presentation.sheikh.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.grand.ezkorone.data.favorite.repository.RepositoryFavorite
import com.grand.ezkorone.data.sheikh.repository.RepositorySheikh
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder.VHItemHomeZekr
import com.grand.ezkorone.presentation.sheikh.adapter.viewModel.VHItemSheikh

class RVItemSheikh(val repoSheikh: RepositorySheikh, private val gson: Gson) : PagingDataAdapter<ItemSheikh, VHItemSheikh>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemSheikh>() {
            override fun areItemsTheSame(
                oldItem: ItemSheikh,
                newItem: ItemSheikh
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemSheikh,
                newItem: ItemSheikh
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemSheikh {
        return VHItemSheikh(parent, this, gson)
    }

    override fun onBindViewHolder(holder: VHItemSheikh, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
