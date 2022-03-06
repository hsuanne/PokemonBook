package com.example.pokemonbook

import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class PokemonViewModel(private val repository: PokemonRepository) : ViewModel() {

    val typeTitleList = MutableLiveData<List<String>>()
    var pokeL: List<Pokemon> = listOf()
    val pokeTypeList: MutableList<MutableLiveData<List<Pokemon>>> = mutableListOf()
    var pokeSingle = MutableLiveData<Pokemon>()
    var pokeIndex = MutableLiveData<Int>()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            if (pokeL.isEmpty()){
                viewModelScope.launch {
                    fetchJson()
                }.join()
                pokeL = repository.getPokemon()
            }
            viewModelScope.launch {
                typeTitleList.value = getTypeTitle()
                categorizePokemon()
                println("pokeL:"+pokeL.size)
                println("pokeTL:"+pokeTypeList.size)
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

    fun setCurrentPokemon(pokemon: Pokemon){
        pokeSingle.value = pokemon
        for(p in MainActivity.pokeL){
            if (p.name.equals(pokemon.name)){
                editPoke(p)
            }
        }
    }

    fun editPoke(pokemon: Pokemon){
        var indexP = MainActivity.pokeL.indexOf(pokemon)
        pokeIndex.value = indexP
        MainActivity.pokeL[indexP] = pokemon
    }

    fun getTypeTitle(): List<String> {
        var typeAll: MutableList<String> = mutableListOf()
        for (i in pokeL) {
            var isSame = false
            for (k in typeAll){
                if (i.typeofpokemon[0] == k){
                    isSame = true
                }
            }
            if (!isSame){
                typeAll.add(i.typeofpokemon[0])
            }
        }
        return typeAll.toList()
    }

    //先把pokemon依照type全部分類好 (pokeTypeList)
    fun categorizePokemon() {
        val tmpTitle = typeTitleList.value!!
        for (j in tmpTitle) {
            var tmpL: MutableList<Pokemon> = mutableListOf()
//            val currentType = typeList[j]
            for (i in pokeL) {
//                val p = pokeL[i]
//                val t = p.typeofpokemon[0]
                if (i.typeofpokemon[0] == j) {
                    tmpL.add(i)
                }
            }
            val tmpLive=MutableLiveData<List<Pokemon>>()
            tmpLive.value = tmpL
            pokeTypeList.add(tmpLive)
        }
    }

    fun sortByMethod(sort_method:String){
        for (i in pokeTypeList){
            var pokemon_filtered:List<Pokemon> = i.value!!
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

    suspend fun fetchJson() {
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
                MainActivity.pokeL = result.toMutableList()
                println("pokeL: " + MainActivity.pokeL.size) //809
                insertAll(*result.toTypedArray())
            }
        })
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
