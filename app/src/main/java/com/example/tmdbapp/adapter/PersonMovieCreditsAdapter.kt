package com.example.tmdbapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tmdbapp.R
import com.example.tmdbapp.helper.RetrofitHelper
import com.example.tmdbapp.model.people.Cast

class PersonMovieCreditsAdapter(private val itemOnClick: (Cast) -> Unit) :
    RecyclerView.Adapter<PersonMovieCreditsAdapter.PersonMovieCreditsViewHolder>() {

    private var movieCredits: MutableList<Cast> = emptyList<Cast>().toMutableList()

    inner class PersonMovieCreditsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImageView: ImageView = itemView.findViewById(R.id.rv_item_person_moviePoster)
        val movieNameTextView: TextView = itemView.findViewById(R.id.rv_item_person_movieName)
        val movieRoleTextView: TextView = itemView.findViewById(R.id.rv_item_person_movieRole)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonMovieCreditsViewHolder {
        return PersonMovieCreditsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_person_moviecredits, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PersonMovieCreditsViewHolder, position: Int) {
        holder.movieImageView.load(
            "${RetrofitHelper.IMAGE_BASE_URL}${movieCredits[position].poster_path}"
        )
        holder.movieNameTextView.text = movieCredits[position].original_title
        val text = "as ${movieCredits[position].character}"
        holder.movieRoleTextView.text = text

        holder.itemView.setOnClickListener {
            itemOnClick.invoke(movieCredits[position])
        }
    }

    override fun getItemCount(): Int {
        return movieCredits.size
    }

    @SuppressLint("NotifyDataSetChanged") // Entire list gets loaded through API.
    fun submitList(movieCreditsList: List<Cast>) {
        movieCredits = movieCreditsList.toMutableList()
        notifyDataSetChanged()
    }

}