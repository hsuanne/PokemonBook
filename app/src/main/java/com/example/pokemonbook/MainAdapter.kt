package com.example.pokemonbook

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView


class MainAdapter(
    val onClick: (PokeAdapter, String) -> Unit,
    val pokemonViewModel: PokemonViewModel, val mainActivity: MainActivity
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    private var pokemon: MutableList<Pokemon> = mutableListOf()
//    val typeAll: MutableList<String> = mutableListOf()
    var typeAll:List<String> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.type_row, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //顯示type label
        when (holder) {
            is TypeViewHolder -> {
                if (typeAll.isNotEmpty()) {
                    var isClick = false
                    //顯示每個type
                    val str = typeAll[position]
                    println("typeview" + str)
                    holder.type_textview.text = str
                    holder.type_textview.setBackgroundColor(Color.parseColor(getColorCode(str)))

                    val pokeAdapter = PokeAdapter(pokemonViewModel, mainActivity)

                    //若type被點擊
                    holder.type_textview.setOnClickListener {
                        val recycler_poke:RecyclerView = holder.itemView.findViewById(R.id.recyclerView_poke)
                        if (!isClick) {
                            recycler_poke.adapter = pokeAdapter
                            onClick(pokeAdapter, str)
                            isClick = true
                        } else {
                            recycler_poke.adapter = null
                            isClick = false
                        }
                    }
                }
            }
        }
    }

//    fun refreshItem(pokemon:MutableList<Pokemon>){
//        this.pokemon = pokemon
//        if (typeAll.isEmpty()){
//            getType()
//            println("typeall:"+typeAll.size)//18
//            println("typeFirst:"+typeAll[0])//18
//        }
//        notifyDataSetChanged()
//    }

    fun refresh(typeTitleList:List<String>){
        this.typeAll = typeTitleList
         println("typeall:"+typeAll.size)//18
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return typeAll.size
    }

    interface OnClickListener {
        fun onClickType(adapter: PokeAdapter, string: String)
    }

    //把屬性放進typeAll列表中
//    fun getType(){
//        for (i in 0 until (pokemon.size-1)){
//            val p = pokemon[i]
//            val t = p.typeofpokemon[0]
//            if (typeAll.isEmpty()){
//                typeAll.add(t)
//            } else {
//                if (!typeAll.contains(t)) typeAll.add(t)
//            }
//        }
//    }

    fun getColorCode(type:String):String{
        return when (type.toLowerCase()) {
            "grass" -> "#2CDAB1"
            "fire" -> "#F7706B"
            "water" -> "#58ABF6"
            "bug" -> "#2CDA90"
            "normal" -> "#7986CB"
            "poison" -> "#9F5BBA"
            "electric" -> "#FFE252"
            "ground" -> "#CA8179"
            "fairy" -> "#FF86AF"
            "fighting" -> "#F78C6B"
            "psychic" -> "#FFCE4B"
            "rock" -> "#955A54"
            "ghost" -> "#7C5BBA"
            "ice" -> "#58F1F6"
            "dragon" -> "#7A9A64"
            "dark" -> "#303943"
            "steel" -> "#4A425A"
            "flying" -> "#C379CB"
            else -> "#7986CB"
        }
    }
}

class TypeViewHolder(view:View):RecyclerView.ViewHolder(view){
    val type_textview:TextView = view.findViewById(R.id.type_textview)
}
