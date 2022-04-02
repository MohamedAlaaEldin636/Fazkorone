package com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder

import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.databinding.ItemTextInCardBinding
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections

class VHItemTextInCard(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_text_in_card, parent)
) {

    private val binding = ItemTextInCardBinding.bind(itemView)

    init {
        binding.materialCardView.setOnClickListener { view ->
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener
            val name = binding.textView.text?.toString().orEmpty()

            view.findNavControllerOfProject().navigate(
                BottomNavFragmentDirections.actionDestBottomNavToDestZekrDetails(id, name)
            )
        }
    }

    fun bind(item: IdAndName) {
        binding.materialCardView.tag = item.id

        binding.textView.text = item.name
    }

}
