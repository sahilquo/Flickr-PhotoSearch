package com.quovantis.photosearch.utils

object ValueUtils {

    fun String.getDescriptionText(): String {
        val data = this.replace("</p>", "").split("<p>")
        return data.last().replace(Regex("\\<.*?\\>"), "").trim()
    }

    fun String.getAuthorName(): String? {
        return this.replace(")","").split("\"").findLast {
            it.isNotEmpty()
        }
    }
}