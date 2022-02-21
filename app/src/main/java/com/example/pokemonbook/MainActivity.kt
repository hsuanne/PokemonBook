package com.example.pokemonbook

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val pokemonViewModel: PokemonViewModel by viewModels {
        PokemonViewModelFactory((application as PokemonApplication).repository)
    }

    companion object {
        var pokeL: MutableList<Pokemon> = mutableListOf() //用gson抓到的資料會存進這個List
        var sort_method: String = "default"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView_main: RecyclerView = findViewById(R.id.recyclerView_main)
        recyclerView_main.layoutManager = LinearLayoutManager(this)

        val adapter = MainAdapter(pokemonViewModel, this)
        recyclerView_main.adapter = adapter

        //第一次執行可以清空DB
//        pokemonViewModel.deletAll()
        println("pokemonDB:"+pokemonViewModel.pokeL)
//        如果DB沒有資料才fetch

        pokemonViewModel.typeTitleList.observe(this) { typeTitleList ->
                adapter.refresh(typeTitleList)
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

    fun fetchJson(adapter: MainAdapter) {
        println("Attempting to Fetch JSON")

        val url =
            "https://gist.githubusercontent.com/mrcsxsiq/b94dbe9ab67147b642baa9109ce16e44/raw/97811a5df2df7304b5bc4fbb9ee018a0339b8a38"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        //requestCall: 非同步
        client.newCall(request).enqueue(object : Callback { //callback:做完之後再回傳結果
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute Request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
                val arr = JSONArray(body) //將資料轉成JSONArray
                println(arr.length()) //809

                val gson = GsonBuilder().create()
                val result =
                    gson.fromJson<List<Pokemon>>(body, object : TypeToken<List<Pokemon>>() {}.type)
                pokeL = result.toMutableList()
                println("pokeL: " + pokeL.size) //809
                pokemonViewModel.insertAll(*result.toTypedArray())
            }
        })
    }
}