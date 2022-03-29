package com.grand.ezkorone.domain.utils.common

import com.grand.ezkorone.domain.aboutUs.ItemAboutUs

data class InitialDataForApp(
    var socialMedia: SocialMedia,
    var aboutUsItems: List<ItemAboutUs>
)
