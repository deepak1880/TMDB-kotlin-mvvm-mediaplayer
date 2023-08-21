package com.example.tmdbapp.adapter;

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


class CastAdapter(private val itemOnClick: (Cast) -> Unit) :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {
    private var cast : MutableList<Cast> = emptyList<Cast>().toMutableList()

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val castImageView: ImageView = itemView.findViewById(R.id.rv_item_image)
        val castNameTextView: TextView = itemView.findViewById(R.id.rv_item_name)
        val castRoleTextView: TextView = itemView.findViewById(R.id.rv_item_role)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_detail_cast, parent, false)
        )
    }

    override fun getItemCount(): Int {
       return cast.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.castImageView.load(RetrofitHelper.IMAGE_BASE_URL + cast[position].profile_path)
        holder.castNameTextView.text = cast[position].name
        holder.castRoleTextView.text = cast[position].character

        holder.itemView.setOnClickListener{
            itemOnClick.invoke(cast[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged") // Entire list gets loaded through API.
    fun submitList(castList: List<Cast>) {
        cast = castList.toMutableList()
        notifyDataSetChanged()
    }


}
