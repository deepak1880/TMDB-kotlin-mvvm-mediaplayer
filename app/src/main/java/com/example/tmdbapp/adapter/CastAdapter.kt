package com.example.tmdbapp.adapter;

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.example.tmdbapp.R
import com.example.tmdbapp.databinding.RvItemDetailCastBinding
import com.example.tmdbapp.helper.NetworkHelper
import com.example.tmdbapp.model.people.Cast


class CastAdapter(private val itemOnClick: (Cast) -> Unit) :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {
    private var cast: MutableList<Cast> = emptyList<Cast>().toMutableList()

    lateinit var context: Context

    private lateinit var binding: RvItemDetailCastBinding

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val castImageView: ImageView = binding.rvItemDetailPersonImage
        val castNameTextView: TextView = binding.rvItemDetailPersonName
        val castRoleTextView: TextView = binding.rvItemDetailPersonRole
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        binding = RvItemDetailCastBinding.inflate(inflater, parent, false)
        return CastViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.castImageView.load(NetworkHelper.IMAGE_BASE_URL + cast[position].profile_path)

        val imageLoader = ImageLoader.Builder(context)
            .build()

        val imageRequest = ImageRequest.Builder(context)
            .data("${NetworkHelper.IMAGE_BASE_URL}${cast[position].profile_path}")
            .placeholder(R.drawable.placeholder_person)
            .target(holder.castImageView)
            .build()
        imageLoader.enqueue(imageRequest)

        holder.castNameTextView.text = cast[position].name
        holder.castRoleTextView.text = cast[position].character

        holder.itemView.setOnClickListener {
            itemOnClick.invoke(cast[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged") // Entire list gets loaded through API.
    fun submitList(castList: List<Cast>) {
        cast = castList.toMutableList()
        notifyDataSetChanged()
    }
}
