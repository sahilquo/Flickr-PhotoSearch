package com.quovantis.photosearch.model

import java.io.Serializable

data class PhotoItem(
    val author: String,
    val description: String,
    val media: Media,
    val title: String
) : Serializable