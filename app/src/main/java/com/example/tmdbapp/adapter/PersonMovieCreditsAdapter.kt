package com.example.tmdbapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.model.people.Cast

class PersonMovieCreditsAdapter(private val itemOnClick: (Cast) -> Unit) :
    RecyclerView.Adapter<PersonMovieCreditsAdapter.PersonMovieCreditsViewHolder>() {

    inner class PersonMovieCreditsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    private var cast: MutableList<Cast> = emptyList<Cast>().toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonMovieCreditsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PersonMovieCreditsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}