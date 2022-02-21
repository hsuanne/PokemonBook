package com.example.pokemonbook

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var typePoke: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.type_text, parent, false)
        return TypePokeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TypePokeViewHolder -> {
                val type = typePoke[position]
                holder.type_textview.text = type

            }
        }
    }

    override fun getItemCount(): Int {
        return typePoke.size
    }

    fun refreshItems(typePoke:MutableList<String>){
        this.typePoke = typePoke
        notifyDataSetChanged()
    }

}

class TypePokeViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val type_textview:TextView = view.findViewById(R.id.detail_type)
}