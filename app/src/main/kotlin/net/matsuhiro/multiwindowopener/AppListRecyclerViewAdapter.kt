package net.matsuhiro.multiwindowopener

import android.animation.ObjectAnimator
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

class AppListRecyclerViewAdapter(val appInfos: List<AppInfo>, val listener: OnItemClickListener?) : RecyclerView.Adapter<AppItemViewHolder>() {

    val decelerateInterpolator = DecelerateInterpolator()
    private var prevPos = 0

    override fun onBindViewHolder(holder: AppItemViewHolder?, position: Int) {
        val appInfo = appInfos[position]
        holder?.appItemView?.nameView?.text = appInfo.name
        holder?.appItemView?.packageView?.text = appInfo.packageName
        holder?.appItemView?.iconView?.setImageDrawable(appInfo.iconDrawable)

        holder?.appItemView?.setOnClickListener { v: View -> Unit
            listener?.onClick(position, v, appInfo)
        }
        var scrollToDown : Boolean = false
        if (prevPos < position) {
            scrollToDown = true
        }
        setAnimation(holder?.appItemView, scrollToDown)
        prevPos = position
    }

    override fun getItemCount(): Int =
            appInfos.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : AppItemViewHolder =
        AppItemViewHolder(AppItemView(parent?.context))

    private fun setAnimation(view: View?, scrollToDown: Boolean): Unit {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f)
        val transY = ObjectAnimator.ofFloat(view, "translationY", (if (scrollToDown) 3.0f else -3.0f) * (if (view != null) view.height.toFloat() else 1.0f), 0.0f)
        arrayOf(alpha, transY).forEach {
            it.interpolator = decelerateInterpolator
            it.setDuration(500).start()
        }
    }

    interface OnItemClickListener {
        fun onClick(pos: Int, view: View, info: AppInfo)
    }
}
