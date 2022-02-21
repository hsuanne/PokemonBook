package com.example.pokemonbook

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "pokemon_table")
data class Pokemon(
    //@SerializedName("name")
    @PrimaryKey(autoGenerate = true)
    val pokemonId:Int = 0,

    @ColumnInfo(name="name")
    val name: String?,

    @ColumnInfo(name="id")
    val id: String?,

    @ColumnInfo(name="imageurl")
    val imageurl: String?,

    @ColumnInfo(name="xdescription")
    val xdescription: String?,

    @ColumnInfo(name="ydescription")
    val ydescription: String?,

    @ColumnInfo(name="height")
    val height: String?,

    @ColumnInfo(name="category")
    val category: String?,

    @ColumnInfo(name="weight")
    val weight: String?,

    @ColumnInfo(name="typeofpokemon")
    val typeofpokemon: MutableList<String>,

    @ColumnInfo(name="hp")
    val hp: Int,

    @ColumnInfo(name="attack")
    val attack: Int,

    @ColumnInfo(name="defense")
    val defense: Int,

    @ColumnInfo(name="speed")
    val speed: Int,

    @ColumnInfo(name="total")
    val total: Int,

    @ColumnInfo(name="favStatus")
    var favStatus:Int
                   ):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        mutableListOf<String>().apply { parcel.readStringList(this)},
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pokemonId)
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(imageurl)
        parcel.writeString(xdescription)
        parcel.writeString(ydescription)
        parcel.writeString(height)
        parcel.writeString(category)
        parcel.writeString(weight)
        parcel.writeStringList(typeofpokemon)
        parcel.writeInt(hp)
        parcel.writeInt(attack)
        parcel.writeInt(defense)
        parcel.writeInt(speed)
        parcel.writeInt(total)
        parcel.writeInt(favStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }

}