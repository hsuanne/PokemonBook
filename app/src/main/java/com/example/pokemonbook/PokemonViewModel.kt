package com.example.pokemonbook

import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    val typeTitleList = MutableLiveData<List<String>>()
    var pokeL: List<Pokemon> = listOf()
    val pokeTypeList: MutableList<MutableLiveData<List<Pokemon>>> = mutableListOf()
    val initialTypeList = mutableMapOf<String, List<Pokemon>>()
    var pokeSingle = MutableLiveData<Pokemon>()
    var pokeIndex = MutableLiveData<Int>()

    init {
        viewModelScope.launch {
            launch {
                pokeL = repository.getPokemon()
                if (pokeL.isEmpty()) {
                    fetchJson()
                    pokeL = repository.getPokemon()
                }
            }.join()

            launch {
                typeTitleList.value = getTypeTitle()
                categorizePokemon()
                println("pokeL:" + pokeL.size)
                println("pokeTL:" + initialTypeList.size)
            }
        }
    }

    fun insert(pokemon: Pokemon) = viewModelScope.launch {
        //透過repository把資料存進database
        repository.insert(pokemon)
    }

    fun insertAll(vararg pokemons: Pokemon) = viewModelScope.launch {
        //透過repository把資料存進database
        repository.insertAll(*pokemons)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun setCurrentPokemon(pokemon: Pokemon) {
        pokeSingle.value = pokemon
        for (p in MainActivity.pokeL) {
            if (p.name.equals(pokemon.name)) {
                editPoke(p)
            }
        }
    }

    fun editPoke(pokemon: Pokemon) {
        var indexP = MainActivity.pokeL.indexOf(pokemon)
        pokeIndex.value = indexP
        MainActivity.pokeL[indexP] = pokemon
    }

    private fun getTypeTitle(): List<String> {
        val types = pokeL.map {
            it.typeofpokemon
        }.flatten().distinct()
        println("types: $types")
        return types
    }

    //先把pokemon依照type全部分類好 (pokeTypeList)
    private fun categorizePokemon() {
//        val tmpTitle = typeTitleList.value!!
//        for (j in tmpTitle) {
//            val pokemonByTypes: MutableList<Pokemon> = mutableListOf()
//            for (pokemon in pokeL) {
//                if (pokemon.typeofpokemon[0] == j) {
//                    pokemonByTypes.add(pokemon)
//                }
//            }
//            val tmpLive = MutableLiveData<List<Pokemon>>()
//            tmpLive.value = pokemonByTypes
//            pokeTypeList.add(tmpLive)
//        }

        val categories = mutableListOf<Pair<String, Pokemon>>()
        for (pokemon in pokeL) {
            for (type in pokemon.typeofpokemon) {
                categories.add(Pair(type, pokemon))
            }
        }
        categories.groupBy {
            it.first
        }.forEach{
            val tmpPokemons = it.value.map { it.second }
            initialTypeList[it.key] = tmpPokemons
        }
//        for (item in initialTypeList) {
//            println(item.key + ", "+ item.value.size)
//        }
    }

    fun sortByMethod(sort_method: String) {
        for (i in pokeTypeList) {
            var pokemon_filtered: List<Pokemon> = i.value!!
            if (sort_method == "DEFAULT") {
                pokemon_filtered = pokemon_filtered.sortedWith(compareBy { it.id }).toMutableList()
                println("1:" + pokemon_filtered[0].attack)
            }

            if (sort_method == "ATK") {
                pokemon_filtered =
                    pokemon_filtered.sortedWith(compareByDescending { it.attack }).toMutableList()
            }

            if (sort_method == "DEF") {
                pokemon_filtered =
                    pokemon_filtered.sortedWith(compareByDescending { it.defense }).toMutableList()
            }

            if (sort_method == "SPD") {
                pokemon_filtered =
                    pokemon_filtered.sortedWith(compareByDescending { it.speed }).toMutableList()
            }
            i.value = pokemon_filtered
        }
    }

    private suspend fun fetchJson() {
        withContext(Dispatchers.Default) {
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

                    val gson = GsonBuilder().create()
                    val result =
                        gson.fromJson<List<Pokemon>>(
                            body,
                            object : TypeToken<List<Pokemon>>() {}.type
                        )
                    MainActivity.pokeL = result.toMutableList()
                    println("JsonPokeL: " + MainActivity.pokeL.size) //809
                    insertAll(*result.toTypedArray())
                }
            })
        }
    }
}

class PokemonViewModelFactory(private val repository: PokemonRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
