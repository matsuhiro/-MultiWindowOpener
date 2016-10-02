package net.matsuhiro.multiwindowopener

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.analytics.FirebaseAnalytics
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    var firebaseAnalytics: FirebaseAnalytics? = null
    var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAdView()

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val appInfos : MutableList<AppInfo> = mutableListOf()
        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AppListRecyclerViewAdapter(appInfos, object : AppListRecyclerViewAdapter.OnItemClickListener {
            override fun onClick(pos: Int, view: View, info: AppInfo) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.component = ComponentName(info.packageName, info.activityName)
                startActivityForResult(intent, 0)
                sendLog(info.packageName)
            }
        })

        val offset: Int = resources.displayMetrics.density.toInt() * 8
        recyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State): Unit{
                val pos = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                if (pos == 0) outRect.set(offset, offset, offset, offset)
                else          outRect.set(offset,      0, offset, offset)
            }
        })

        rx.Observable.from(packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0))
                .map { info: ResolveInfo ->
                    val name = packageManager.getApplicationLabel(info.activityInfo.applicationInfo) as String
                    val packageName = info.activityInfo.packageName
                    val icon = packageManager.getApplicationIcon(packageName)
                    val activityName = info.activityInfo.name
                    AppInfo(icon, name, packageName, activityName)
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: rx.Observer<AppInfo> {
                    override fun onCompleted() {
                    }
                    override fun onError(e: Throwable?) {}
                    override fun onNext(info: AppInfo) {
                        appInfos.add(info)
                        recyclerView.adapter.notifyItemInserted(appInfos.size-1)
                    }
                })
    }

    private fun sendLog(packageName: String) : Unit {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, packageName)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "package")
        firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun setupAdView(): Unit {
        adView = findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }
}
