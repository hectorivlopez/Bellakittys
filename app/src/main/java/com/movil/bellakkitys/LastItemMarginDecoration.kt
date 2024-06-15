package com.movil.bellakkitys

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemMarginDecoration(private val marginBottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        // Add bottom margin to the last item
        if (position == itemCount - 1) {
            outRect.bottom = marginBottom
        } else {
            outRect.bottom = 0
        }
    }
}