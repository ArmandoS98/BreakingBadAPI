package com.example.breakingbadapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breakingbadapi.Adapters.BreakingBadAdapter
import com.example.breakingbadapi.Pojos.Character
import com.example.breakingbadapi.RetrofitServices.ICharacters
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerAdapter: BreakingBadAdapter

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.breakingbadapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = retrofit.create(ICharacters::class.java).getAllCharacters()

        call.enqueue(object : Callback<List<Character>> {
            override fun onResponse(
                call: Call<List<Character>>,
                response: Response<List<Character>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Success")

                    val characters = response.body()

                    characters?.forEach {
                        Log.d(TAG, "-> ${it.nickname}")
                    }

                    characters?.let { recyclerInit(it) }

                } else
                    Log.e(TAG, "onResponse: ${response.code()}")
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun recyclerInit(it: List<Character>) {
        recyclerAdapter = BreakingBadAdapter(this) { item, position, status ->
            Log.d(TAG, "onCreate: ${item.nickname}")

            it[position].isSelected = status
            Log.d(TAG, "onCreate: ${it[position].isSelected}")

            recyclerAdapter.setSuperHeroes(it)
        }
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = recyclerAdapter
        recyclerAdapter.setSuperHeroes(it)
    }
}