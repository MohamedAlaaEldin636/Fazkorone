package com.grand.ezkorone.domain.taspeh

data class ResponseTaspeh(
    var list: List<ItemTaspeh>,
    var currentPage: Int,
    var hasPrevPage: Boolean,
    var hasNextPage: Boolean,
)
