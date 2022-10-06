package com.example.pokemonbook

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(){
    companion object{
        @JvmStatic
        fun start(context: Context, pokemon: Pokemon) {
            val starter = Intent(context, DetailActivity::class.java)
                .putExtra("name", pokemon)
            context.startActivity(starter)
        }
    }

    private val pokemonViewModel: PokemonViewModel by viewModels {
        PokemonViewModelFactory((application as PokemonApplication).repository)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //open bundle
        val pokemon = intent.extras?.getParcelable<Pokemon>("name")
        if (pokemon!=null) {
            pokemonViewModel.setCurrentPokemon(pokemon)
//            for (p in pokeL){
//                if (p.name.equals(pokemon.name)){
//                    var ind = pokeL.indexOf(p)
//                    pokeL[ind] = pokemon
//                    println("pokeL_p:" + p.favStatus)
//                }
//            }
        }

        pokemonViewModel.pokeSingle.observe(this){ it ->
                if (it != null) {
                        Toast.makeText(this, "it_favStatus:${it.favStatus}", Toast.LENGTH_SHORT).show()
                        bind(it)
                    }
        }

        val leftButton = findViewById<TextView>(R.id.left_button)
        val rightButton = findViewById<TextView>(R.id.right_button)

        var pokeL = pokemonViewModel.pokeL
        pokemonViewModel.pokeIndex.observe(this){ index ->
            leftButton.setOnClickListener {
                var index_b = index - 1
                if(index_b<0) {
                    index_b = pokeL.size-1
                }
                pokemonViewModel.setCurrentPokemon(pokeL[index_b])
                pokemonViewModel.pokeIndex.value = index_b
                Toast.makeText(this, "click_fav:"+ pokeL[index_b].favStatus, Toast.LENGTH_SHORT).show()
            }
            rightButton.setOnClickListener {
                var index_b = index + 1
                if (index_b>= pokeL.size){
                    index_b = 0
                }
                pokemonViewModel.setCurrentPokemon(pokeL[index_b])
                pokemonViewModel.pokeIndex.value = index_b
            }
        }

    }

    fun bind(currentPokemon:Pokemon){
        findViewById<TextView>(R.id.detail_pokemon_name).text = currentPokemon.name
        findViewById<TextView>(R.id.detail_id).text = currentPokemon.id
        val recyclerView_detail: RecyclerView = findViewById(R.id.recyclerView_typePoke)
        recyclerView_detail.layoutManager = GridLayoutManager(this, currentPokemon.typeofpokemon.size)
        val adapter = DetailAdapter()
        recyclerView_detail.adapter = adapter
        adapter.refreshItems(currentPokemon.typeofpokemon)
        findViewById<TextView>(R.id.detail_description).text = currentPokemon.xdescription
        findViewById<TextView>(R.id.detail_height).text =
            String.format(getString(R.string.Height_num, currentPokemon.height))
        findViewById<TextView>(R.id.detail_weight).text =
            String.format(getString(R.string.Weight_num, currentPokemon.weight))
        findViewById<TextView>(R.id.detail_total).text =
            getString(R.string.total_num, currentPokemon.total)
        findViewById<TextView>(R.id.detail_HP).text = getString(R.string.HP_num, currentPokemon.hp)
        findViewById<TextView>(R.id.detail_ATK).text = getString(R.string.ATK_num, currentPokemon.attack)
        findViewById<TextView>(R.id.detail_DEF).text = getString(R.string.DEF_num, currentPokemon.defense)
        findViewById<TextView>(R.id.detail_SPD).text = getString(R.string.SPD_num, currentPokemon.speed)
        Glide.with(this).load(currentPokemon.imageurl).into(findViewById<ImageView>(R.id.detail_poke_img))
        findViewById<ConstraintLayout>(R.id.detail_CL1).setBackgroundColor(Color.parseColor(getColorCode(currentPokemon.typeofpokemon[0])))
        val heart_imgview:ImageView = findViewById(R.id.detail_heart)
        if (currentPokemon.favStatus==0){
            heart_imgview.setColorFilter(Color.GRAY)
        } else {
            heart_imgview.setColorFilter(Color.RED)
        }
        heart_imgview.setOnClickListener {
            if (currentPokemon.favStatus == 1){
                currentPokemon.favStatus = 0
                heart_imgview.setColorFilter(Color.GRAY)
            } else {
                currentPokemon.favStatus = 1
                heart_imgview.setColorFilter(Color.RED)
            }
        }
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