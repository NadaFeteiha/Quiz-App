package com.nadafeteiha.quizapp.apis

import com.nadafeteiha.quizapp.data.CategoryResponse
import com.nadafeteiha.quizapp.data.QuestionsResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("api_category.php")
    fun getCategory() : Call<CategoryResponse>

    @GET("api.php?type=multiple")
    fun getQuizQuestions(@Query("id")id: Int,
                         @Query("amount") amount: Int ): Call<QuestionsResponse>

    companion object {
        var BASE_URL = "https://opentdb.com/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}