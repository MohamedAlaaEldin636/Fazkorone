package com.grand.ezkorone.presentation.sheikh.adapter.viewModel

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.ItemSheikhBinding
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.sheikh.SheikhListFragment
import com.grand.ezkorone.presentation.sheikh.adapter.RVItemSheikh

class VHItemSheikh(parent: ViewGroup, private val adapter: RVItemSheikh, private val gson: Gson) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_sheikh, parent)
) {

    private val binding = ItemSheikhBinding.bind(itemView)

    init {
        binding.linearLayout.setOnClickListener { view ->
            val jsonItemSheikh = binding.linearLayout.tag as? String ?: return@setOnClickListener
            val itemSheikh = jsonItemSheikh.fromJson<ItemSheikh>(gson)

            view.executeOnGlobalLoading(
                afterShowingLoading = {
                    adapter.repoSheikh.toggleSheikhSelectionForSalawat(itemSheikh.id).also {
                        if (it is MAResult.Success) {
                            view.findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                SheikhListFragment.SAVED_STATE_SELECTED_JSON_ITEM_SHEIKH,
                                jsonItemSheikh
                            )
                        }
                    }
                },
                afterHidingLoading = { result ->
                    when (result) {
                        is MAResult.Failure -> {
                            view.context.showErrorToast(result.getMessageNotEmptyNorNullOr(
                                view.context.getString(R.string.something_went_wrong)
                            ))
                        }
                        is MAResult.Success -> {
                            adapter.refresh()
                        }
                    }
                }
            )
        }
    }

    fun bind(item: ItemSheikh) {
        binding.linearLayout.tag = item.toJson(gson)

        Glide.with(binding.shapeableImageView)
            .load(item.imageUrl)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(binding.shapeableImageView)

        binding.textView.text = item.name

        binding.checkImageView.setImageResource(
            if (item.selected) R.drawable.ic_button_checked else R.drawable.ic_button_not_checked
        )
    }

}
