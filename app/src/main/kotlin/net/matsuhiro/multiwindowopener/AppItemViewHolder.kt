package net.matsuhiro.multiwindowopener

import android.support.v7.widget.RecyclerView
import android.view.View

class AppItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val appItemView : AppItemView?
    init {
        appItemView = view as AppItemView
    }
}
