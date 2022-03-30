package com.grand.ezkorone.presentation.internalNavigation.adapters.viewHolder

import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.executeOnGlobalLoading
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.databinding.ItemHomeZekrBinding
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import com.grand.ezkorone.presentation.internalNavigation.adapters.RVItemHomeZekr

class VHItemHomeZekr(parent: ViewGroup, private val adapter: RVItemHomeZekr) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_home_zekr, parent)
) {

    private val binding = ItemHomeZekrBinding.bind(itemView)

    init {
        binding.favoriteImageView.setOnClickListener { view ->
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener

            view.executeOnGlobalLoading(
                afterShowingLoading = {
                    adapter.repoFavorite.toggleFavoriteForVerticalList(id)
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

        binding.materialCardView.setOnClickListener { view ->
            val id = binding.materialCardView.tag as? Int ?: return@setOnClickListener
            val name = binding.textView.text?.toString().orEmpty()

            view.findNavControllerOfProject().navigate(
                // todo akid will need id as well isa.
                BottomNavFragmentDirections.actionDestBottomNavToDestAzkarList(name)
            )
        }
    }

    fun bind(item: ItemZekrTopCategory) {
        binding.materialCardView.tag = item.id

        Glide.with(binding.imageImageView)
            .load(item.imageUrl)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(binding.imageImageView)

        binding.favoriteImageView.setImageResource(
            if (item.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart
        )

        binding.textView.text = item.name
    }

}
