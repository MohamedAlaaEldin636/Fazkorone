package com.grand.ezkorone.presentation.taspeh.adapter.viewHolder

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.ItemTaspehBinding
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.taspeh.TaspehListFragment
import com.grand.ezkorone.presentation.taspeh.adapter.RVItemTaspeh
import timber.log.Timber

class VHItemTaspeh(parent: ViewGroup, private val adapter: RVItemTaspeh, private val gson: Gson) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_taspeh, parent)
) {

    private val binding = ItemTaspehBinding.bind(itemView)

    init {
        binding.linearLayout.setOnClickListener { view ->
            val index = binding.textView.tag as? Int ?: return@setOnClickListener

            val json = binding.linearLayout.tag as? String ?: return@setOnClickListener

            adapter.changeSelectedIndex(index)

            view.findNavController().previousBackStackEntry?.savedStateHandle?.set(
                TaspehListFragment.SAVED_STATE_SELECTED_JSON_ITEM_TASPEH,
                json
            )
        }
    }

    fun bind(item: ItemTaspeh, isSelected: Boolean, index: Int) {
        binding.linearLayout.tag = item.toJson(gson)
        binding.textView.tag = index

        binding.textView.text = item.name

        binding.checkImageView.setImageResource(
            if (isSelected) R.drawable.ic_button_checked else R.drawable.ic_button_not_checked
        )
    }

}
