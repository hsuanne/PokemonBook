package com.example.pokemonbook

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    companion object {
        var sort_method: String = "default"
    }

    private val pokemonViewModel: PokemonViewModel by viewModels {
        PokemonViewModelFactory((application as PokemonApplication).repository)
    }

    val pokeAdapter = PokeAdapter()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView_main: RecyclerView = findViewById(R.id.recyclerView_main)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        var lastVisibleItemIndex = 0

        val adapter = MainAdapter(
            onTypeClicked = { position, recyclerView ->
                if (position == null) {
                    pokemonViewModel.setIsPokemonTypeShown(false)
                    return@MainAdapter null
                } else {
                    pokemonViewModel.setIsPokemonTypeShown(true)
                    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            val lm = recyclerView.layoutManager as GridLayoutManager
                            lastVisibleItemIndex = lm.findLastCompletelyVisibleItemPosition()
                        }
                    })
                    refreshPokemonAdapter(position)
                }
            }
        )

        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = adapter

        recyclerView_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                pokemonViewModel.loadMore(dy, lastVisibleItemIndex)
            }
        })

        //第一次執行可以清空DB
//        pokemonViewModel.deletAll()
        // FIXME: first time open app doesn't work

        with(pokemonViewModel){
            typeTitleList.observe(this@MainActivity) { typeTitleList ->
                adapter.refresh(typeTitleList)
            }

            showProgressBar.observe(this@MainActivity) {
                showProgressBar(it, progressBar)
            }

            pokemonsByType.observe(this@MainActivity){
                pokeAdapter.refreshItems(it.toMutableList())
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
        pokemonViewModel.refreshViewModelOnTypeClicked(position)
        return pokeAdapter
    }
}