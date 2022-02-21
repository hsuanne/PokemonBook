package com.example.pokemonbook

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(Pokemon::class), version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PokemonRoomDatabase :RoomDatabase() {
    abstract fun pokemonDao():PokemonDao
    companion object{
        @Volatile
        private var INSTANCE:PokemonRoomDatabase? = null

//        val migration_1_3: Migration = object:Migration(1,3){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE pokemon_table ADD COLUMN favStatus INTEGER NOT NULL DEFAULT 0")
//            }
//        }

        fun getDatabase(context: Context, scope:CoroutineScope):PokemonRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonRoomDatabase::class.java,
                    "pokemon_database"
                )
//                    .fallbackToDestructiveMigration()
//                    .addMigrations(migration_1_3)
                    .addCallback(PokemonDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PokemonDatabaseCallback(private val scope:CoroutineScope):RoomDatabase.Callback(){
    }
}

class Converters {
    @TypeConverter
    fun listToJson(value: MutableList<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toMutableList()
}