package com.grand.ezkorone.presentation.search.adapter.viewModel

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.executeOnGlobalLoading
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.databinding.ItemHomeZekrBinding
import com.grand.ezkorone.databinding.ItemSearchQueryBinding
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.search.ItemSearchQuery
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import com.grand.ezkorone.presentation.internalNavigation.adapters.RVItemHomeZekr
import com.grand.ezkorone.presentation.search.SearchQueriesFragmentDirections

class VHSearchQueries(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_search_query, parent)
) {

    private val binding = ItemSearchQueryBinding.bind(itemView)

    init {
        binding.textView.setOnClickListener { view ->
            val id = binding.textView.tag as? Int ?: return@setOnClickListener

            view.findNavController().navigate(
                SearchQueriesFragmentDirections.actionDestSearchQueriesToDestZekrDetails(
                    id, binding.textView.text?.toString().orEmpty()
                )
            )
        }
    }

    fun bind(item: ItemSearchQuery) {
        binding.textView.tag = item.id

        binding.textView.text = item.name
    }

}
