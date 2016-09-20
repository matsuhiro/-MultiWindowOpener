package net.matsuhiro.multiwindowopener

import android.support.annotation.IdRes
import android.view.View

fun <T : View> View.bindView(@IdRes id: Int): Lazy<T> = lazy {
    @Suppress("UNCHECKED_CAST")
    (findViewById(id) as T)
}
