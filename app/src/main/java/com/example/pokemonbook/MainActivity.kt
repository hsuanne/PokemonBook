package com.example.pokemonbook

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*

class MainActivity : AppCompatActivity() {
    private val pokemonViewModel: PokemonViewModel by viewModels {
        PokemonViewModelFactory((application as PokemonApplication).repository)
    }

    companion object {
        var pokeL: MutableList<Pokemon> = mutableListOf() //用gson抓到的資料會存進這個List
        var sort_method: String = "default"
    }

    val pokeAdapter = PokeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView_main: RecyclerView = findViewById(R.id.recyclerView_main)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        val adapter = MainAdapter(
            onTypeClicked = {refreshPokemonAdapter(it)}
        )

        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = adapter

        //第一次執行可以清空DB
//        pokemonViewModel.deletAll()
        println("pokemonDB:"+pokemonViewModel.pokeL)

        with(pokemonViewModel){
            typeTitleList.observe(this@MainActivity) { typeTitleList ->
                adapter.refresh(typeTitleList)
            }

            showProgressBar.observe(this@MainActivity) {
                showProgressBar(it, progressBar)
            }
        }

        val default_tab: TextView = findViewById(R.id.tab_default)
        val atk_tab: TextView = findViewById(R.id.tab_atk)
        val def_tab: TextView = findViewById(R.id.tab_def)
        val spd_tab: TextView = findViewById(R.id.tab_spd)
        default_tab.setOnClickListener {
            sort_method = "default"
            pokemonViewModel.sortByMethod(sort_method)
        }
        atk_tab.setOnClickListener {
            sort_method = "atk"
            pokemonViewModel.sortByMethod(sort_method)
            it.setBackgroundColor(resources.getColor(R.color.teal_200))
        }
        def_tab.setOnClickListener {
            sort_method = "def"
            pokemonViewModel.sortByMethod(sort_method)
        }
        spd_tab.setOnClickListener {
            sort_method = "spd"
            pokemonViewModel.sortByMethod(sort_method)
        }
    }

    private fun showProgressBar(it: Boolean, progressBar: ProgressBar) {
        if (it) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.INVISIBLE
    }

    private fun refreshPokemonAdapter(position: Int): PokeAdapter {
        pokemonViewModel.pokeTypeList[position].observe(this){
            pokeAdapter.refreshItems(it.toMutableList())
        }
        return pokeAdapter
    }
}