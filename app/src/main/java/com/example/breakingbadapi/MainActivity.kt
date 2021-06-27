package com.example.breakingbadapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        recyclerAdapter = BreakingBadAdapter(this) { item ->
            Log.d(TAG, "onCreate: ${item.nickname}")
        }
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = recyclerAdapter

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

                    characters?.let { recyclerAdapter.setSuperHeroes(it) }

                } else
                    Log.e(TAG, "onResponse: ${response.code()}")
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}