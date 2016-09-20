package net.matsuhiro.multiwindowopener

import android.graphics.drawable.Drawable

data class AppInfo(val iconDrawable: Drawable,
                   val name: String,
                   val packageName: String,
                   val activityName: String)
