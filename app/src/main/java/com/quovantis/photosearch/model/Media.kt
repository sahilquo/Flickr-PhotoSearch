package com.quovantis.photosearch.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Media(
    @SerializedName("m")
    val mediaLink: String
) : Serializable