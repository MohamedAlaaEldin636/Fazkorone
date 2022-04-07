package com.grand.ezkorone.presentation.drawer.adapters.viewHolder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.executeOnGlobalLoading
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.databinding.ItemFavZekrDetailBinding
import com.grand.ezkorone.databinding.ItemHomeZekrBinding
import com.grand.ezkorone.databinding.ItemNotificationBinding
import com.grand.ezkorone.domain.favorite.ItemFavorite
import com.grand.ezkorone.domain.notifications.ItemNotification
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.drawer.FavoriteFragmentDirections
import com.grand.ezkorone.presentation.drawer.adapters.RVItemFavorite
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections

abstract class VHItemFavorite(parent: ViewGroup, @LayoutRes layoutRes: Int) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(layoutRes, parent)
) {
    abstract fun bind(item: ItemFavorite)
}

class VHItemFavZekrDetail(parent: ViewGroup, private val adapter: RVItemFavorite) : VHItemFavorite(parent, R.layout.item_fav_zekr_detail) {

    private val binding = ItemFavZekrDetailBinding.bind(itemView)

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
                FavoriteFragmentDirections.actionDestFavoriteToDestZekrDetails(
                    id,
                    true,
                    id,
                    name
                )
            )
        }
    }

    override fun bind(item: ItemFavorite) {
        binding.materialCardView.tag = item.id

        binding.textView.text = item.name
    }

}

class VHItemHomeZekr2(parent: ViewGroup, private val adapter: RVItemFavorite) : VHItemFavorite(parent, R.layout.item_home_zekr) {

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
                FavoriteFragmentDirections.actionDestFavoriteToDestAzkarList(id, name)
            )
        }
    }

    override fun bind(item: ItemFavorite) {
        binding.materialCardView.tag = item.id

        binding.textView.text = item.name

        Glide.with(binding.imageImageView)
            .load(item.imageUrl)
            .apply(RequestOptions().centerCrop())
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(binding.imageImageView)

        binding.favoriteImageView.setImageResource(R.drawable.ic_heart_filled)
    }

}
