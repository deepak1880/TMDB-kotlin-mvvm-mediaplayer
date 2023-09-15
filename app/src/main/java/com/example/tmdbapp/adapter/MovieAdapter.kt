package com.example.tmdbapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.example.tmdbapp.R
import com.example.tmdbapp.databinding.RvSubitemHomeSectionMovieBinding
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.model.Movie

class MovieAdapter(val itemOnClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    lateinit var context: Context

    private var movies: MutableList<Movie> = emptyList<Movie>().toMutableList()

    private lateinit var binding: RvSubitemHomeSectionMovieBinding

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = binding.homeListImages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = RvSubitemHomeSectionMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(movies[position].posterPath!=null){
            Log.e("movie test - in if", "${movies[8].posterPath}", )
            val imageLoader = ImageLoader.Builder(context)
                .build()
            val imageRequest = ImageRequest.Builder(context)
                .data(
                    NetworkHelper.IMAGE_BASE_URL + movies[position].posterPath
                )
                .placeholder(R.drawable.placeholder_movies)
                .target(holder.imageView)
                .build()
            imageLoader.enqueue(imageRequest)
        }
        else{
            Log.e("movie test in else", "${movies[position]}", )
            holder.imageView.load(R.drawable.no_image_available_placeholder)
        }

        holder.itemView.setOnClickListener {
            itemOnClick.invoke(movies[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged") // Entire list gets loaded through API.
    fun submitList(moviesList: List<Movie>?) {
        movies.clear()
        movies.addAll(moviesList ?: emptyList())
        notifyDataSetChanged()
    }
}