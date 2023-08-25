package com.example.tmdbapp.helper;

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemMarginDecorationHelper {
    class GridItemMarginDecoration(private val spaceSize: Int) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val pos = parent.getChildAdapterPosition(view)
            with(outRect) {
                if (pos < (parent.layoutManager as GridLayoutManager).spanCount) {
                    top = spaceSize
                }
                left = spaceSize
                right = spaceSize
                bottom = spaceSize
            }

        }
    }

    class HorizontalItemMarginDecoration(private val spaceSize: Int) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    left = spaceSize
                }
                top = spaceSize
                right = spaceSize
                bottom = spaceSize
            }

        }
    }

    class VerticalItemMarginDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    top = spaceSize

                }
                left = spaceSize
                right = spaceSize
                bottom = spaceSize
            }

        }
    }
}
