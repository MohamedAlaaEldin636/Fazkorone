package com.grand.ezkorone.presentation.drawer.adapters.viewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.databinding.ItemTextBinding
import com.grand.ezkorone.databinding.ItemWhoAreWeBinding
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs

class VHItemWhoAreWe(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_who_are_we, parent)
) {

    private val binding = ItemWhoAreWeBinding.bind(itemView)

    fun bind(item: ItemAboutUs) {
        binding.titleTextView.text = item.title
        binding.bodyTextView.text = item.description
    }

}
