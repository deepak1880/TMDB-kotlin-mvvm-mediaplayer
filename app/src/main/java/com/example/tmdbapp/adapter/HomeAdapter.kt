package com.example.tmdbapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.databinding.RvItemHomeSectionBinding
import com.example.tmdbapp.extensions.gone
import com.example.tmdbapp.helper.ItemMarginDecorationHelper
import com.example.tmdbapp.helper.Status
import com.example.tmdbapp.model.Movie
import com.example.tmdbapp.model.recyclerviewsmodel.HomeModel
import com.facebook.shimmer.ShimmerFrameLayout

class HomeAdapter(private val onClick: (Movie) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var homeModelList = mutableListOf<HomeModel>(
        HomeModel("Popular", emptyList(), Status.LOADING),//0
        HomeModel("Top Rated", emptyList(),Status.LOADING),
        HomeModel("Upcoming", emptyList(),Status.LOADING),
        HomeModel("Now Playing", emptyList(),Status.LOADING)
    )

    private lateinit var binding: RvItemHomeSectionBinding

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = binding.rvItemHomeTvTitle
        val sectionRecyclerView: RecyclerView = binding.rvItemHomeRvMovies
        val shimmer: ShimmerFrameLayout = binding.shimmerNowPlayingContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RvItemHomeSectionBinding.inflate(inflater, parent, false)
        return HomeViewHolder(
            binding.root
        )
    }

    override fun getItemCount(): Int {
        return homeModelList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val horizontalItemMarginDecoration =
            ItemMarginDecorationHelper.HorizontalItemMarginDecoration(10)
        holder.textView.text = homeModelList[position].title
        holder.sectionRecyclerView.setHasFixedSize(true)
        holder.sectionRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MovieAdapter {
            onClick.invoke(it)
        }
        holder.sectionRecyclerView.adapter = adapter
        holder.sectionRecyclerView.addItemDecoration(horizontalItemMarginDecoration)
        adapter.submitList(homeModelList[position].movies)
        when(homeModelList[position].status){
            Status.LOADING ->{
                holder.shimmer.startShimmer()
            }
            Status.SUCCESS -> holder.shimmer.gone()
            Status.ERROR -> {}
        }
    }

    fun submitData(data: HomeModel) {
        homeModelList.add(data)
        notifyItemInserted(homeModelList.size - 1)
    }

    fun updateDataItem(index: Int, dataMovies: List<Movie>,status:Status) {
        homeModelList[index].movies = dataMovies
        homeModelList[index].status=status
        notifyItemChanged(index)
    }
}