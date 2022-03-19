package com.example.pokemonbook

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokemonbook.MainActivity.Companion.pokeL

class PokeAdapter(val pokemonViewModel: PokemonViewModel, val mainActivity: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var pokemon_filtered = mapOf<String, List<Pokemon>>()
    private var pokemons = listOf<Pokemon>()
    private lateinit var type: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.poke_row, parent, false)
        return PokeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PokeViewHolder -> {
                pokemons = pokemon_filtered.values.map { it }[0]
                type = pokemon_filtered.keys.first()
                val pokeItem = pokemons[position]
                var pokeNew = pokeItem

                println("PokeViewHolder: ${pokemons.size}, $type, $pokeItem")
                holder.apply {
                    poke_layout.setBackgroundColor(Color.parseColor(getColorCode(type)))
                    pokename_textview.text = pokeItem.name
                    order_textview.text = pokeItem.id
                    atk_textview.text = holder.atk_textview.context.getString(R.string.ATK_num, pokeItem.attack)
                    def_textview.text = holder.def_textview.context.getString(R.string.DEF_num, pokeItem.defense)
                    spd_textview.text = holder.spd_textview.context.getString(R.string.SPD_num, pokeItem.speed)
                    Glide.with(mainActivity.applicationContext).load(pokeItem.imageurl).into(holder.poke_imgview)
                    if (pokeItem.favStatus==0){
                        heart_imgview.setColorFilter(Color.GRAY)
                    } else {
                        heart_imgview.setColorFilter(Color.RED)
                    }
                    heart_imgview.setOnClickListener {
                        if (pokeItem.favStatus == 1){
                            pokeItem.favStatus = 0
                            heart_imgview.setColorFilter(Color.GRAY)
                            for (p in pokeL){
                                if (p.name == pokeItem.name){
                                    p.favStatus = 0
                                    pokeNew = p
                                    return@setOnClickListener
                                }
                            }
                        } else {
                            pokeItem.favStatus = 1
                            heart_imgview.setColorFilter(Color.RED)
//                            for (p in pokeL){
//                                if (pokeItem.name.equals(p.name)){
//                                    p.favStatus = 1
////                                    var ind = pokeL.indexOf(p)
////                                    pokeL[ind] = pokeItem
//                                    pokeNew = p
//                                    println("favStatus:"+ pokeNew.favStatus)
//                                    return@setOnClickListener
//                                }
//                            }
                        }
                    }
                }
                holder.poke_layout.setOnClickListener {
                    DetailActivity.start(holder.itemView.context, pokeItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return pokemon_filtered.values.map { it }[0].size
    }

    fun refreshItems(pokemonMap: Map<String, List<Pokemon>>) {
        this.pokemon_filtered = pokemonMap
//        if (sort_method == "atk"){
//            this.pokemon_filtered = pokemon_filtered.sortedWith(compareBy{it.attack}).toMutableList()
//            println("1:" + pokemon_filtered[0].attack + "3:" +pokemon_filtered[3].attack)
//        } else {
//            this.pokemon_filtered = pokemon_filtered
//        }
        notifyDataSetChanged()
    }

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

class PokeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val poke_layout: ConstraintLayout = view.findViewById(R.id.poke_layout)
    val pokename_textview: TextView = view.findViewById(R.id.pokemon_name)
    val order_textview: TextView = view.findViewById(R.id.order_num)
    val atk_textview: TextView = view.findViewById(R.id.atk_num)
    val def_textview: TextView = view.findViewById(R.id.def_num)
    val spd_textview: TextView = view.findViewById(R.id.spd_num)
    val poke_imgview: ImageView = view.findViewById(R.id.poke_img)
    val heart_imgview:ImageView = view.findViewById(R.id.heart)
}