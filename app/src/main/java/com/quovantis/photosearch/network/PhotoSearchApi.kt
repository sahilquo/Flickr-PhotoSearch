package com.quovantis.photosearch.network

import com.quovantis.photosearch.model.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoSearchApi {

    @GET("photos_public.gne?format=json&nojsoncallback=1")
    suspend fun search(@Query("tags") tags: String): PhotoResponse?
}