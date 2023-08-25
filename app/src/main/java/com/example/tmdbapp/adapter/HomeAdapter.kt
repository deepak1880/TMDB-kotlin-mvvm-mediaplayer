package com.example.tmdbapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.recyclerviews.HomeRecyclerView

class HomeAdapter(private val onClick: (Movie) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var homeRecyclerViewList: List<HomeRecyclerView> = emptyList()

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.rv_item_home_tv_title)
        val sectionRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_item_home_rv_movies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_home_section, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return homeRecyclerViewList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val horizontalItemMarginDecoration =
            ItemMarginDecorationHelper.HorizontalItemMarginDecoration(10)
        holder.textView.text = homeRecyclerViewList[position].title
        holder.sectionRecyclerView.setHasFixedSize(true)
        holder.sectionRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MovieAdapter {
            onClick.invoke(it)
        }
        holder.sectionRecyclerView.adapter = adapter
        holder.sectionRecyclerView.addItemDecoration(horizontalItemMarginDecoration)
        adapter.submitList(homeRecyclerViewList[position].movies)

    }

    fun submitList(list: List<HomeRecyclerView>) {
        homeRecyclerViewList = list
        notifyDataSetChanged()
    }

}