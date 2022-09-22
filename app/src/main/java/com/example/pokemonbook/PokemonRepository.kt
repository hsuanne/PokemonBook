package com.example.pokemonbook

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val pokemonDao: PokemonDao) {
    suspend fun getPokemon():List<Pokemon>{
        return withContext(Dispatchers.IO) {
            pokemonDao.getPokemon()
        }
    }

    suspend fun insert(pokemon:Pokemon){
        withContext(Dispatchers.IO) {
            pokemonDao.insert(pokemon)
        }
    }

    suspend fun insertAll(vararg pokemons: Pokemon) {
        withContext(Dispatchers.IO) {
            pokemonDao.insertAll(*pokemons)
        }
    }

    suspend fun deleteAll(){
        withContext(Dispatchers.IO) {
            pokemonDao.deleteAll()
        }
    }
}