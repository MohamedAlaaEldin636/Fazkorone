package com.grand.ezkorone.presentation.taspeh.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.grand.ezkorone.data.sheikh.repository.RepositorySheikh
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.presentation.sheikh.adapter.viewModel.VHItemSheikh
import com.grand.ezkorone.presentation.taspeh.adapter.viewHolder.VHItemTaspeh

class RVItemTaspeh(private val gson: Gson) : PagingDataAdapter<ItemTaspeh, VHItemTaspeh>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemTaspeh>() {
            override fun areItemsTheSame(
                oldItem: ItemTaspeh,
                newItem: ItemTaspeh
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ItemTaspeh,
                newItem: ItemTaspeh
            ): Boolean = oldItem == newItem
        }
    }

    private var selectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemTaspeh {
        return VHItemTaspeh(parent, this, gson)
    }

    override fun onBindViewHolder(holder: VHItemTaspeh, position: Int) {
        holder.bind(getItem(position) ?: return, position == selectedIndex, position)
    }

    fun changeSelectedIndex(index: Int) {
        if (index == selectedIndex) {
            return
        }

        val indicesToBeNotifiedForChange = mutableListOf(index)
        if (selectedIndex != -1) {
            indicesToBeNotifiedForChange += selectedIndex
        }

        selectedIndex = index

        indicesToBeNotifiedForChange.forEach {
            notifyItemChanged(it)
        }
    }

}
