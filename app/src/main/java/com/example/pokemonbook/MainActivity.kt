package com.example.pokemonbook

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val pokemonViewModel: PokemonViewModel by viewModels {
        PokemonViewModelFactory((application as PokemonApplication).repository)
    }
    private lateinit var recyclerViewMain: RecyclerView
    private lateinit var mainAdapter: MainAdapter
    private lateinit var pokeAdapter: PokeAdapter
    private lateinit var defaultTab: TextView
    private lateinit var attackTab: TextView
    private lateinit var defenseTab: TextView
    private lateinit var speedTab: TextView
    private lateinit var tabArray: ArrayList<TextView>

    companion object {
        var pokeL: MutableList<Pokemon> = mutableListOf() //用gson抓到的資料會存進這個List
        var sortMethod: String = "DEFAULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 第一次執行可以清空DB
//         pokemonViewModel.deleteAll()

        // 如果DB沒有資料才fetch
        pokemonViewModel.typeTitleList.observe(this) { typeTitleList ->
            mainAdapter.refresh(typeTitleList)
        }

        val onClick : (PokeAdapter, String) -> Unit = { pokeAdapter: PokeAdapter, string: String ->
            pokeAdapter.refreshItems(pokemonViewModel.initialTypeList.filter { it.key == string })
        }

        recyclerViewMain = findViewById(R.id.recyclerView_main)
        with(recyclerViewMain){
            layoutManager = LinearLayoutManager(this@MainActivity)
            mainAdapter = MainAdapter(onClick, pokemonViewModel, this@MainActivity)
                .apply { adapter = this }
        }

        defaultTab = findViewById(R.id.tab_default)
        attackTab = findViewById(R.id.tab_atk)
        defenseTab = findViewById(R.id.tab_def)
        speedTab = findViewById(R.id.tab_spd)

        tabArray = ArrayList()
        tabArray.add(defaultTab)
        tabArray.add(attackTab)
        tabArray.add(defenseTab)
        tabArray.add(speedTab)

        defaultTab.setOnClickListener {
            sortMethod = resources.getString(R.string.default_tab)
            pokemonViewModel.sortByMethod(sortMethod)
            updateTabColor()
        }
        attackTab.setOnClickListener {
            sortMethod = resources.getString(R.string.attack_tab)
            pokemonViewModel.sortByMethod(sortMethod)
            updateTabColor()
        }
        defenseTab.setOnClickListener {
            sortMethod = resources.getString(R.string.defense_tab)
            pokemonViewModel.sortByMethod(sortMethod)
            updateTabColor()
        }
        speedTab.setOnClickListener {
            sortMethod = resources.getString(R.string.speed_tab)
            pokemonViewModel.sortByMethod(sortMethod)
            updateTabColor()
        }
    }

    private fun updateTabColor(){
        for (tab in tabArray) {
            if (tab.text == sortMethod) tab.background = resources.getDrawable(R.drawable.selected_tab_bg)
            else tab.background = resources.getDrawable(R.drawable.unselected_tab_bg)
        }
    }
}