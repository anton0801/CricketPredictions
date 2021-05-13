package com.cricketpredictions.app.ui.fragments.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cricketpredictions.app.MainActivity
import com.cricketpredictions.app.R
import com.cricketpredictions.app.api.App
import com.cricketpredictions.app.model.AuthResponse
import com.amplitude.api.Amplitude
import com.cricketpredictions.app.database.REF_DATABASE_ROOT
import com.cricketpredictions.app.database.initFirebase
import com.cricketpredictions.app.database.isShowAdAvailable
import com.cricketpredictions.app.model.ShowingAds
import com.cricketpredictions.app.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

const val ACCESS_TOKEN_KEY = "access_token_key"

class SplashActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var sharedManager: SharedManager

    private lateinit var refListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        MobileAds.initialize(this) { }

        sharedManager = SharedManager(applicationContext)

        (application as App).getApi().auth(
            "9c0eed6cb8de84b2b6953142fe272a38",
            "f6f29296b7c04fbd90425c48a5d5f587",
            "Cricket predictions",
            "developer"
        ).enqueue(AppRetrofitCallback<AuthResponse> {
            val authResponse = it.body()!!
            val auth = authResponse.auth
            sharedManager.putString(ACCESS_TOKEN_KEY, auth.accessToken)
            Handler().postDelayed({
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                finish()
                startActivity(intent)
                Amplitude.getInstance().logEvent("Inter")
            }, 500)
        })
    }

}