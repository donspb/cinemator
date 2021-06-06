package ru.gb.androidone.donspb.cinematron.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gb.androidone.donspb.cinematron.model.OneMovie

interface TmdbApi {
    @GET("3/movie/{movieID}")
    fun getMovie(
        @Path("movieID") movieID: Int,
        @Query("api_key") token: String
    ): Call<OneMovie>
}