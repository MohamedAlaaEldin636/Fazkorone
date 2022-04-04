package com.grand.ezkorone.presentation.drawer.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs
import com.grand.ezkorone.presentation.drawer.adapters.viewHolder.VHItemWhoAreWe
import com.grand.ezkorone.presentation.location.adapter.viewModel.VHItemText

class RVItemWhoAreWe : ListAdapter<ItemAboutUs, VHItemWhoAreWe>(COMPARATOR) {

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<ItemAboutUs>() {
            override fun areItemsTheSame(
                oldItem: ItemAboutUs,
                newItem: ItemAboutUs
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ItemAboutUs,
                newItem: ItemAboutUs
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemWhoAreWe {
        return VHItemWhoAreWe(parent)
    }

    override fun onBindViewHolder(holder: VHItemWhoAreWe, position: Int) {
        holder.bind(getItem(position) ?: return)
    }
}
