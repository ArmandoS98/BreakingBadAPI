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

        getFavorites()

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

                    //Analizamos
                    val favorites = getFavorites()?.split(",")

                    // Lista definitiva
                    var temp: List<Character?>
                    var temp2: List<Character>
                    var counter = 0

                    for (i in characters!!.indices) {//analizo el array que viene del API
                        for (j in favorites!!.indices) {//Analizo solo los favoritos
                            if (characters[i].char_id == favorites[j].toInt()) {
                                //los fav que hagan match
                                characters[i].isSelected = true
                                //temp?.toMutableList()?.add(counter, characters[i])
                                counter++
                            } else {
                                //los que no esten en fav
                                //temp2?.toMutableList()?.add(counter, characters[i])
                                counter++
                            }
                        }
                    }

                    Log.d(TAG, "onResponse: ")

                    //val valor = (temp?.size?.minus(1))
                    //for (i in valor!! until characters.size) {
                    //   Log.d(TAG, "onResponse: ${temp!![i]?.name}")
                    //}

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

            //save to favorites
            if (status)
                SharedPrefManager().setStringPrefVal(
                    applicationContext,
                    getString(R.string.kfav),
                    addToFavorites(item.char_id)
                )
            else {
                //Eliminar
                val temp = getFavorites()
                val list = temp?.split(",")
                var nueva = ""
                for (i in list!!.indices) {
                    if (!list[i].equals(item.char_id)) {
                        if (nueva.isEmpty())
                            nueva = list[i]
                        else {
                            nueva = "$nueva,${list[i]}"
                        }
                    }
                }

                Log.d(TAG, "recyclerInit: $nueva")

                SharedPrefManager().setStringPrefVal(
                    applicationContext,
                    getString(R.string.kfav),
                    nueva
                )

                getFavorites()
            }

            it[position].isSelected = status
            recyclerAdapter.setSuperHeroes(it)
        }
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = recyclerAdapter
        recyclerAdapter.setSuperHeroes(it)
    }

    private fun getFavorites() =
        SharedPrefManager().getStringVal(applicationContext, getString(R.string.kfav))

    private fun addToFavorites(charId: Int?): String? {
        val temp = SharedPrefManager().getStringVal(applicationContext, getString(R.string.kfav))
        var register = ""
        if (temp!!.isEmpty()) {
            register = charId.toString()
        } else {
            register = "$temp,$charId"
        }
        return register
    }


}