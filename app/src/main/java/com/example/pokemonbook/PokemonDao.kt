package com.example.pokemonbook

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_table")
    suspend fun getPokemon(): List<Pokemon>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pokemon:Pokemon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg pokemons: Pokemon)

    @Query("SELECT COUNT (*) FROM pokemon_table")
    fun getCount():Int

    @Query("DELETE FROM pokemon_table")
    suspend fun deleteAll()
}