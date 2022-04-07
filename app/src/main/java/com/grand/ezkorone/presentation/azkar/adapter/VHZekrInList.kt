package com.grand.ezkorone.presentation.azkar.adapter

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.databinding.ItemSearchQueryBinding
import com.grand.ezkorone.databinding.ItemZekrInListBinding
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.search.ItemSearchQuery
import com.grand.ezkorone.presentation.azkar.AzkarListFragmentDirections
import com.grand.ezkorone.presentation.search.SearchQueriesFragmentDirections

class VHZekrInList(parent: ViewGroup, private val idToBeUsedForFavoriteToggle: Int) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_zekr_in_list, parent)
) {

    private val binding = ItemZekrInListBinding.bind(itemView)

    init {
        binding.materialCardView.setOnClickListener { view ->
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            view.findNavController().navigate(
                AzkarListFragmentDirections.actionDestAzkarListToDestZekrDetails(
                    id,
                    false,
                    idToBeUsedForFavoriteToggle,
                    binding.textView.text?.toString().orEmpty()
                )
            )
        }
    }

    fun bind(item: ItemZekrInList) {
        binding.materialCardView.tag = item.id

        binding.textView.text = item.name
    }

}
