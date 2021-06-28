package com.example.breakingbadapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.breakingbadapi.Pojos.Character
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.item_card.*
import java.io.Serializable

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val currentCharacter: Character =
            intent.getSerializableExtra(getString(R.string.key_character)) as Character

        Log.d("val", "onCreate: ${currentCharacter.name}")

        views(currentCharacter)

    }

    private fun views(cc: Character) {
        tvCharacterName.text = cc.name
        tvNickName.text = cc.nickname
    }
}