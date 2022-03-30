package com.grand.ezkorone.presentation.location.adapter.viewModel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.databinding.ItemTextBinding
import com.grand.ezkorone.databinding.ItemTextInCardBinding
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections

class VHItemText(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_text, parent)
) {

    private val binding = ItemTextBinding.bind(itemView)

    fun bind(item: String) {
        binding.textView.text = item
    }

}
