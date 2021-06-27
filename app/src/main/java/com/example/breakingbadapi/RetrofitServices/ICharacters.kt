package com.example.breakingbadapi.RetrofitServices

import com.example.breakingbadapi.Pojos.Character
import retrofit2.Call
import retrofit2.http.GET

interface ICharacters {
    //get all Elements
    @GET("characters")
    fun getAllCharacters(): Call<List<Character>>
}