package net.matsuhiro.multiwindowopener

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


class AppListAdapter(private val context: Context) : BaseAdapter() {

    var appInfos: List<AppInfo> = emptyList()

    override fun getCount(): Int = appInfos.size

    override fun getItem(position: Int): Any? = appInfos[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
            ((convertView as? AppItemView) ?: AppItemView(context)).apply {
                setAppInfo(appInfos[position])
            }
}
