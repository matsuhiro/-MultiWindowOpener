package net.matsuhiro.multiwindowopener

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class AppItemView : FrameLayout {

    constructor(context : Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    val iconView: ImageView by bindView(R.id.app_item_icon)
    val nameView: TextView by bindView(R.id.app_item_name)
    val packageView: TextView by bindView(R.id.app_item_package)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_app_item, this)
    }

    fun setAppInfo(appInfo: AppInfo) {
        nameView.text = appInfo.name
        packageView.text = appInfo.packageName
        iconView.setImageDrawable(appInfo.iconDrawable)
    }
}
