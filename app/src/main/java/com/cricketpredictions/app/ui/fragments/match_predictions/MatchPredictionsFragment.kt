package com.cricketpredictions.app.ui.fragments.match_predictions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplitude.api.Amplitude
import com.cricketpredictions.app.INTERSTITIAL_AD_ID
import com.cricketpredictions.app.R
import com.cricketpredictions.app.database.MATCH_PREDICTIONS_NODE
import com.cricketpredictions.app.database.REF_DATABASE_ROOT
import com.cricketpredictions.app.database.getPredictionModel
import com.cricketpredictions.app.model.match_prediction.MatchPrediction
import com.cricketpredictions.app.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yandex.metrica.YandexMetrica
import java.text.SimpleDateFormat
import java.util.*

class MatchPredictionsFragment : Fragment(), MatchPredictionAdapter.OnReadBtnClickedListener {

    private var mInterstitialAd: InterstitialAd? = null

    private var predictions = listOf<MatchPrediction>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var matchPredictionAdapter: MatchPredictionAdapter

    private lateinit var progressBar: ProgressBar

    private lateinit var toolbar: Toolbar
    private lateinit var dateInToolbar: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_match_predictions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            recyclerView = findViewById(R.id.match_predictions_recycler_view)
            progressBar = findViewById(R.id.progress_bar)

            toolbar = findViewById(R.id.predictions_toolbar)
            toolbar.background.alpha = 128
            APP_ACTIVITY.setSupportActionBar(toolbar)
            dateInToolbar = findViewById(R.id.date_in_toolbar)
        }

        recyclerView.apply {
            matchPredictionAdapter = MatchPredictionAdapter()
            matchPredictionAdapter.setOnReadBtnClickedListener(this@MatchPredictionsFragment)
            adapter = matchPredictionAdapter
            layoutManager = LinearLayoutManager(APP_ACTIVITY)
        }
    }

    override fun onStart() {
        super.onStart()
        progressBar.visibility = View.VISIBLE
        // parseMatchPredictions()
        getMatchs()
        APP_ACTIVITY.mainToolbar.visibility = View.GONE
        APP_ACTIVITY.title = "Cricket Predictions"
    }

    private fun getMatchs() {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        REF_DATABASE_ROOT.child(MATCH_PREDICTIONS_NODE)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    predictions = snapshot.children.map { it.getPredictionModel() }
                    Collections.sort(
                        predictions,
                        Comparator { o1, o2 ->
                            return@Comparator o1.human_time.compareTo(o2.human_time)
                        })

                    val predictionA = arrayListOf<MatchPrediction>()
                    for (prediction in predictions) {
                        val predictionMonth = prediction.human_time.timestampToMonth()
                        val predictionDay = prediction.human_time.timestampToDay()
                        if (predictionDay.toInt() >= currentDay && predictionMonth.toInt() >= currentMonth) {
                            predictionA.add(prediction)
                        } else if (predictionMonth.toInt() > currentMonth) {
                            predictionA.add(prediction)
                        }
                    }

                    if (predictionA.isEmpty()) {
                        if (predictions.size > 2) {
                            predictionA.add(predictions[predictions.size - 2].apply { name += " (archived)" })
                            predictionA.add(predictions[predictions.size - 1].apply { name += " (archived)" })
                        } else {
                            predictionA.addAll(predictions.apply {
                                this.forEach {
                                    it.name += " (archived)"
                                }
                            })
                        }
                    }
                    matchPredictionAdapter.setMatchs(predictionA)
                    progressBar.visibility = View.GONE
                }
            })
    }

    override fun onReadBtnClicked(matchPrediction: MatchPrediction) {
        Amplitude.getInstance().logEvent("Prediction Open")
        YandexMetrica.reportEvent("READ")
        if (isShowingAds.isShow) {
            progressBar.visibility = View.VISIBLE
            InterstitialAd.load(
                APP_ACTIVITY,
                INTERSTITIAL_AD_ID,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d("ADS_LOAD_ERROR", p0.message)
                        with(matchPrediction) {
                            replaceFragment(
                                MatchPredictionShowFragment.newInstance(
                                    name,
                                    place,
                                    human_time,
                                    tips,
                                    team_a.full_name,
                                    team_b.full_name,
                                    prediction
                                ), true
                            )
                        }
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        mInterstitialAd = p0
                        progressBar.visibility = View.GONE
                        if (mInterstitialAd != null) {
                            mInterstitialAd!!.show(APP_ACTIVITY)
                        }
                        with(matchPrediction) {
                            replaceFragment(
                                MatchPredictionShowFragment.newInstance(
                                    name,
                                    place,
                                    human_time,
                                    tips,
                                    team_a.full_name,
                                    team_b.full_name,
                                    prediction
                                ), true
                            )
                        }
                    }
                })
        } else {
            with(matchPrediction) {
                replaceFragment(
                    MatchPredictionShowFragment.newInstance(
                        name,
                        place,
                        human_time,
                        tips,
                        team_a.full_name,
                        team_b.full_name,
                        prediction
                    ), true
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mainToolbar.visibility = View.VISIBLE
        APP_ACTIVITY.setSupportActionBar(APP_ACTIVITY.mainToolbar)
    }

}