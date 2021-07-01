package com.example.breakingbadapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.breakingbadapi.Pojos.Character
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(topAppBar)
        val actionbar = supportActionBar
        actionbar!!.title = ""
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val currentCharacter: Character =
            intent.getSerializableExtra(getString(R.string.key_character)) as Character

        views(currentCharacter)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun views(cc: Character) {
        if (cc.isSelected) {
            laHeart.speed = 1f
            laHeart.playAnimation()
        }

        tvCharacterName.text = cc.name
        tvNickName.text = cc.nickname

        Glide.with(this)
            .load(cc.img)
            .into(ivImgN)

        tvBirthday.text = cc.birthday

        var occupation = ""
        cc.occupation?.forEach {
            occupation += it + "\n"
        }

        tvOccupationN.text = occupation

        tvStatusN.text = cc.status

        tvPortrayedN.text = cc.portrayed

        val apper = cc.appearance
        var append = ""
        for (i in apper!!.indices) {
            if (i == 0) {
                append = apper[i].toString()
            } else {
                append += ", ${apper[i]}"
            }
        }
        tvAppearance.text = append

        tvCategory.text = cc.category

        val bcsa = cc.better_call_saul_appearance
        append = ""
        if (bcsa!!.isNotEmpty()) {
            for (i in bcsa.indices) {
                if (i == 0) {
                    append = bcsa[i].toString()
                } else {
                    append += ", ${bcsa[i]}"
                }
            }
        } else
            append = "None"

        tvBetterCallSaulAppearance.text = append
    }
}