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
        Log.d(TAG, "recyclerInit: ${getFavorites()}")

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

                    Log.d(TAG, "onResponse: ${favorites!!.size}")
                    // Lista definitiva
                    var counter = 0
                    if (favorites.isNotEmpty() && favorites[0] != "") {
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
                    }

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
            if (status) {
                SharedPrefManager().setStringPrefVal(
                    applicationContext,
                    getString(R.string.kfav),
                    addToFavorites(item.char_id)
                )
                Log.d(TAG, "recyclerInit: ${getFavorites()}")

            } else {
                //Eliminar
                Log.d(TAG, "recyclerInit: ELIMINANDO ACCION")
                val temp = getFavorites()
                val repl = item.char_id
                var list = temp!!.replace("$repl", "")
                var esComa = false
                var current = ""

                val charItems = list.toCharArray()

                val favorites = removeFavorites(charItems)
               // Log.d(TAG, "Almacenar: $favorites")
                SharedPrefManager().setStringPrefVal(
                    applicationContext,
                    getString(R.string.kfav),
                    favorites
                )
            }

            it[position].isSelected = status
            recyclerAdapter.setSuperHeroes(it)
        }
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = recyclerAdapter
        recyclerAdapter.setSuperHeroes(it)
    }

    private fun removeFavorites(charItems: CharArray): String {
        var favorites = ""
        for (i in charItems.indices) {
            //Log.d(TAG, "char -> pos[$i]: ${charItems[i]}")
            if (charItems[0] == ',') {
                //No hacer nada
                charItems[i] = ' '
                //Log.d(TAG, "removeFavorites: Primera es una coma")
            } else {
                //validamos si hay comas al ingresar aqui
                val item = charItems[i]
                val isComa = item == ','
                if (isComa) {
                    //Validamos la posicion anterior
                    val itemBefore = charItems[i - 1]
                    val isComa2 = itemBefore == ','
                    val isComa3 = itemBefore == ' '
                    if (!isComa2 && !isComa3)
                        favorites += charItems[i]
                } else
                    favorites += charItems[i]
            }
        }

       // Log.d(TAG, "removeFavorites0: $favorites")
        val longitud = favorites.length
        if (longitud > 1) {
            val finalComma = favorites.substring(longitud.minus(1), longitud)
            val isComaa = finalComma.equals(",")
            if (isComaa) {
                val temp = favorites
                favorites = temp.substring(0, longitud - 1)
            }
        }

        return favorites
    }

    private fun getFavorites() =
        SharedPrefManager().getStringVal(applicationContext, getString(R.string.kfav))

    private fun addToFavorites(charId: Int?): String? {
        val temp = SharedPrefManager().getStringVal(applicationContext, getString(R.string.kfav))
        var register: String
        if (temp!!.isEmpty()) {
            register = charId.toString()
        } else {
            register = "$temp,$charId"
        }

        return register
    }


}

