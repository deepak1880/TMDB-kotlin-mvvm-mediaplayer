package com.example.tmdbapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.tmdbapp.R
import com.example.tmdbapp.databinding.RvItemPersonMoviecreditsBinding
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.model.people.Cast

class PersonMovieCreditsAdapter(private val itemOnClick: (Cast) -> Unit) :
    RecyclerView.Adapter<PersonMovieCreditsAdapter.PersonMovieCreditsViewHolder>() {
    lateinit var context : Context

    private var movieCredits: MutableList<Cast> = emptyList<Cast>().toMutableList()

    private lateinit var binding : RvItemPersonMoviecreditsBinding

    inner class PersonMovieCreditsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val movieImageView: ImageView = binding.rvItemPersonMoviePoster
        val movieNameTextView: TextView = binding.rvItemPersonMovieName
        val movieRoleTextView: TextView = binding.rvItemPersonMovieRole
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonMovieCreditsViewHolder {
        context=parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = RvItemPersonMoviecreditsBinding.inflate(inflater,parent,false)
        return PersonMovieCreditsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PersonMovieCreditsViewHolder, position: Int) {
        /*holder.movieImageView.load(
            "${NetworkHelper.IMAGE_BASE_URL}${movieCredits[position].poster_path}"
        )*/

        val imageLoader = ImageLoader.Builder(context)
            .build()

        val imageRequest = ImageRequest.Builder(context)
            .data("${NetworkHelper.IMAGE_BASE_URL}${movieCredits[position].poster_path}")
            .placeholder(R.drawable.placeholder_movies)
            .target(holder.movieImageView)
            .build()
        imageLoader.enqueue(imageRequest)


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