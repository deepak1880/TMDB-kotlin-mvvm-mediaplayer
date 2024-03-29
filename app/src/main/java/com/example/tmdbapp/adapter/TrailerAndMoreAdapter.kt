package com.example.tmdbapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbapp.R
import com.example.tmdbapp.databinding.RvItemTrailersVideosBinding
import com.example.tmdbapp.model.videos.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class TrailerAndMoreAdapter() : RecyclerView.Adapter<TrailerAndMoreAdapter.TrailerViewHolder>() {
    var videos: List<Video> = emptyList()

    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: RvItemTrailersVideosBinding = RvItemTrailersVideosBinding.bind(itemView)
        private val youtubePlayerView: YouTubePlayerView =
            binding.rvItemTrailersVideoPlayer
        private var youtubePlayer: YouTubePlayer? = null
        val videoTitle : TextView = binding.rvItemTrailersVideoName

        init {
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(player: YouTubePlayer) {
                    youtubePlayer = player
                    if (adapterPosition != RecyclerView.NO_POSITION && adapterPosition < videos.size) {
                        val videoId = videos[adapterPosition].key
                        youtubePlayer?.cueVideo(videoId, 0f)
                    }
                }
            })
        }

        fun bind(video: Video) {
           youtubePlayer?.cueVideo(video.key, 0f)
        }

        fun pauseVideo() {
            Log.d("inside pause vid","working")
            youtubePlayer?.pause()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailerAndMoreAdapter.TrailerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.rv_item_trailers_videos, parent, false)
        return TrailerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: TrailerAndMoreAdapter.TrailerViewHolder, position: Int) {
        holder.bind(videos[position])
        val title = "${videos[position].type} : ${videos[position].name}"
        holder.videoTitle.text = title
    }

    fun submitList(videoList: List<Video>) {
        videos = videoList
        notifyDataSetChanged()
    }

    override fun onViewAttachedToWindow(holder: TrailerViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("TAG","attached")
    }
    override fun onViewDetachedFromWindow(holder: TrailerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d("TAG","detached")
    }
}