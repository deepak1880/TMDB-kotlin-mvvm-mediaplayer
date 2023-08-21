package com.example.tmdbapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tmdbapp.R
import com.example.tmdbapp.helper.RetrofitHelper
import com.example.tmdbapp.model.Movie

class MovieAdapter(val itemOnClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: MutableList<Movie> = emptyList<Movie>().toMutableList()


    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.home_list_images)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_home_movie, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.imageView.load(RetrofitHelper.IMAGE_BASE_URL + movies[position].posterPath)

        holder.itemView.setOnClickListener {
            itemOnClick.invoke(movies[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged") // Entire list gets loaded through API.
    fun submitList(moviesList: List<Movie>) {
        movies = moviesList.toMutableList()
        notifyDataSetChanged()
    }

}