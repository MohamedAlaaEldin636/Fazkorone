package com.grand.ezkorone.core.extensions

import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter

/**
 * - Used since I wanna show loading on launch isa.
 *
 * - No need for prepend to check refresh error as if it happens on launch only append will be
 * notified so no 2 errors above each other will happen isa.
 */
fun PagingDataAdapter<*, *>.withCustomAdapters(
	header: LoadStateAdapter<*>,
	footer: LoadStateAdapter<*>
): ConcatAdapter {
	addLoadStateListener { loadStates ->
		header.loadState = if (loadStates.refresh is LoadState.Loading) {
			LoadState.Loading
		}else {
			loadStates.prepend
		}
		footer.loadState = when (loadStates.refresh) {
			is LoadState.Loading -> {
				LoadState.NotLoading(false)
			}
			is LoadState.Error -> {
				loadStates.refresh
			}
			else -> {
				loadStates.append
			}
		}
	}
	
	return ConcatAdapter(header, this, footer)
}
