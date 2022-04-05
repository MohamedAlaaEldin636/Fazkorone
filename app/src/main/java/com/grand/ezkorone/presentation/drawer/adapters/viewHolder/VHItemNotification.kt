package com.grand.ezkorone.presentation.drawer.adapters.viewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.inflateLayout
import com.grand.ezkorone.databinding.ItemNotificationBinding
import com.grand.ezkorone.databinding.ItemTextBinding
import com.grand.ezkorone.domain.notifications.ItemNotification

class VHItemNotification(parent: ViewGroup) : RecyclerView.ViewHolder(
    parent.context.inflateLayout(R.layout.item_notification, parent)
) {

    private val binding = ItemNotificationBinding.bind(itemView)

    fun bind(item: ItemNotification) {
        binding.dateAndTimeTextView.text = item.createdAt

        binding.msgTextView.text = item.message
    }

}
