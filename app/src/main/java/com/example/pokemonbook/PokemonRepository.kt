package com.example.pokemonbook

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class PokemonRepository(private val pokemonDao: PokemonDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPokemon():List<Pokemon>{
        return pokemonDao.getPokemon()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(pokemon:Pokemon){
        pokemonDao.insert(pokemon)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(vararg pokemons: Pokemon) {
        pokemonDao.insertAll(*pokemons)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(){
        pokemonDao.deleteAll()
    }
}