package com.example.pokemonbook

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PokemonApplication : Application(){
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy {PokemonRoomDatabase.getDatabase(this, applicationScope)}
    val repository by lazy {PokemonRepository(database.pokemonDao())}
}