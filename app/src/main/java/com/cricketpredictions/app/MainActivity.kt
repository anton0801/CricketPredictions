package com.cricketpredictions.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.amplitude.api.Amplitude
import com.cricketpredictions.app.api.App
import com.cricketpredictions.app.database.initFirebase
import com.cricketpredictions.app.database.isShowAdAvailable
import com.cricketpredictions.app.ui.fragments.cricket_schedule.CriketScheduleFragment
import com.cricketpredictions.app.ui.fragments.match_predictions.MatchPredictionsFragment
import com.cricketpredictions.app.ui.fragments.news.NewsFragment
import com.cricketpredictions.app.utils.*
import com.cricketpredictions.app.viewmodel.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView

const val INTERSTITIAL_AD_ID = "ca-app-pub-4550723216157488/7210767187"

class MainActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null

    lateinit var mainToolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView

    private var isAdShowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amplitude = Amplitude.getInstance()

        amplitude.initialize(this, "f67d1f16acb0e63fc90e5664ca2f3aec")
        // Enable COPPA (Turning off sensitive data tracking)
        amplitude.enableCoppaControl()

        APP_ACTIVITY = this
        APP = application as App
        VIEW_MODEL = ViewModelProviders.of(this).get(MainViewModel::class.java)
        SHARED_MANAGER = SharedManager()

        mainToolbar = findViewById(R.id.toolbar)
        mainToolbar.background.alpha = 128
        setSupportActionBar(mainToolbar)

        bottomNavigationView = findViewById(R.id.bottom_nav)

        initFirebase()

        val adProgressBar: ProgressBar = findViewById(R.id.ad_progress_bar)

        isShowAdAvailable {
            val adView: AdView = findViewById(R.id.ad_view)
            if (isShowingAds.isShow) {
                adView.visibility = View.VISIBLE
                adView.loadAd(AdRequest.Builder().build())
                amplitude.logEvent("Inter")

                if (!isAdShowed) {
                    adProgressBar.visibility = View.VISIBLE
                    InterstitialAd.load(
                        APP_ACTIVITY,
                        INTERSTITIAL_AD_ID,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(p0: LoadAdError) {
                                Log.d("ADS_LOAD_ERROR", p0.message)
                                adProgressBar.visibility = View.GONE
                                replaceFragment(MatchPredictionsFragment())
                            }

                            override fun onAdLoaded(p0: InterstitialAd) {
                                mInterstitialAd = p0
                                if (mInterstitialAd != null) {
                                    mInterstitialAd!!.show(APP_ACTIVITY)
                                }
                                adProgressBar.visibility = View.GONE
                                replaceFragment(MatchPredictionsFragment())
                            }
                        })
                }
            } else {
                adView.visibility = View.GONE
                if (!isAdShowed)
                    replaceFragment(MatchPredictionsFragment())
            }
        }

        amplitude.logEvent("Session Start")
        if (!SHARED_MANAGER.getBoolean("IS_FIRST_OPEN")) {
            amplitude.logEvent("First Open")
            SHARED_MANAGER.putBoolean("IS_FIRST_OPEN", true)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_match_predictions -> {
                    amplitude.logEvent("Match Predictions")
                    replaceFragment(MatchPredictionsFragment())
                }
                R.id.nav_news -> {
                    amplitude.logEvent("News")
                    replaceFragment(NewsFragment())
                }
                R.id.nav_cricket_schedule -> {
                    amplitude.logEvent("Cricket Schedule")
                    replaceFragment(CriketScheduleFragment())
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

}