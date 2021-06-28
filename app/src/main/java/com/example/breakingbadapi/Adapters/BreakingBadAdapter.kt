package com.example.breakingbadapi.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingbadapi.Pojos.Character
import com.example.breakingbadapi.R
import kotlinx.android.synthetic.main.item_card.view.*

class BreakingBadAdapter(
    private val contexto: Context,
    private val listener: (Character, position: Int, status: Boolean) -> Unit
) :
    RecyclerView.Adapter<BreakingBadAdapter.ViewHolder>() {

    var movieList: List<Character> = listOf()

    fun setSuperHeroes(movieList: List<Character>) {
        this.movieList = movieList;
        //notifyDataSetChanged()
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.item_card, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_name.text = movieList[position].name
        holder.itemView.tv_SuperheroName.text = movieList[position].nickname

        //Image
        Glide.with(contexto)
            .load(movieList[position].img)
            .into(holder.itemView.product_image)

        //Heart - Animation
        val lottieAnim = holder.itemView.laHeart

        //Esto es cuando scroliando y que detecte si algo cambio o no en la vista
        if (movieList[position].isSelected){
            lottieAnim.speed = 1f
            lottieAnim.playAnimation()
        }else{
            lottieAnim.speed = 0f
            lottieAnim.playAnimation()
        }

        //Este se ejecuta cuando el usuario da click
        lottieAnim.setOnClickListener {
            var isCheckt = movieList[position].isSelected
            Log.d(TAG, "onBindViewHolder IN: ${isCheckt}")
            val statusC: Boolean
            if (isCheckt) {
                lottieAnim.speed = 0f
                lottieAnim.playAnimation()
                isCheckt = false
                statusC = isCheckt
                Log.d(TAG, "onBindViewHolder OUT: false")
            } else {
                lottieAnim.speed = 1f
                lottieAnim.playAnimation()
                isCheckt = true
                statusC = isCheckt
                Log.d(TAG, "onBindViewHolder OUT: true")
            }
            listener(movieList[position], position, statusC)
        }
    }

    override fun getItemCount() = movieList.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "onClick: Ejecutandose")
        }

        companion object {
            private val PHOTO_KEY = "KEY"
        }

    }

    companion object {
        private const val TAG = "BreakingBadAdapter"
    }

}