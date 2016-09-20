package net.matsuhiro.multiwindowopener

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
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
        val adapter = AppListAdapter(applicationContext)
        adapter.appInfos = appInfos
        val listView = findViewById(R.id.app_info_list) as ListView
        listView.adapter = adapter

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
                        adapter.notifyDataSetChanged()
                    }
                    override fun onError(e: Throwable?) {}
                    override fun onNext(info: AppInfo) {
                        appInfos.add(info)
                    }
                })

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val info = appInfos[i]
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.component = ComponentName(info.packageName, info.activityName)
            startActivityForResult(intent, 0)
            sendLog(info.packageName)
        }
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
