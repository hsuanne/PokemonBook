package com.example.pokemonbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemonbook.MainActivity.Companion.pokeL

class DetailViewModel:ViewModel() {
    var pokeSingle = MutableLiveData<Pokemon>()

    fun setCurrentPokemon(name:String){
        for (p in pokeL){
            if (name.equals(p.name)){
                pokeSingle.value = p
                return
            }
        }
    }
}