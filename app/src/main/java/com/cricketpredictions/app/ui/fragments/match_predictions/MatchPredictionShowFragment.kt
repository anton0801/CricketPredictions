package com.cricketpredictions.app.ui.fragments.match_predictions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.amplitude.api.Amplitude
import com.cricketpredictions.app.R
import com.cricketpredictions.app.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton
import com.yandex.metrica.YandexMetrica
import java.util.*
import kotlin.collections.ArrayList

class MatchPredictionShowFragment : Fragment() {

    private var name = ""
    private var place = ""
    private var human_time = ""
    private var firstCommandS = ""
    private var secondCommandS = ""
    private var predictionResultS = ""
    private var tipsList = arrayListOf<String>()
    private var urlToOpenBet =
        "https://9wu8vx76.assterteam.com/click.php?key=pwi7c7zh37t4fpblmocg&k1={k1}&k2={k2}&k3={k3}&source=zzz&clickid={clickid}"

    private lateinit var matchName: TextView
    private lateinit var matchStartTime: TextView
    private lateinit var matchDate: TextView
    private lateinit var matchPlace: TextView

    private lateinit var firstCommand: TextView
    private lateinit var secondCommand: TextView
    private lateinit var predictionResult: TextView

    private lateinit var tips: LinearLayout

    private lateinit var placeABetBtn: MaterialButton

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.mainToolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        APP_ACTIVITY.mainToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        APP_ACTIVITY.mainToolbar.title = "Cricket Predictions"
        return inflater.inflate(R.layout.fragment_match_prediction_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            name = arguments?.getString(NAME_KEY).toString()
            place = arguments?.getString(PLACE_KEY).toString()
            human_time = arguments?.getString(HUMAN_TIME_KEY).toString()
            firstCommandS = arguments?.getString(FIRST_COMMAND_KEY).toString()
            secondCommandS = arguments?.getString(SECOND_COMMAND_KEY).toString()
            predictionResultS = arguments?.getString(PREDICTION_RESULT_KEY).toString()
            tipsList = arguments?.getStringArrayList(TIPS_KEY) ?: arrayListOf()
        }

        with(view) {
            matchName = findViewById(R.id.match_name)
            matchStartTime = findViewById(R.id.match_start_time)
            matchDate = findViewById(R.id.match_date)
            matchPlace = findViewById(R.id.place)
            firstCommand = findViewById(R.id.first_command)
            secondCommand = findViewById(R.id.second_command)
            predictionResult = findViewById(R.id.prediction_result)
            tips = findViewById(R.id.tips)
            placeABetBtn = findViewById(R.id.place_a_bet_btn)
            progressBar = findViewById(R.id.progress_bar)
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, human_time.timestampToMonth().toInt() - 1)

        matchName.text =
            if (name.isNotEmpty()) name else "$firstCommandS vs $secondCommandS Match Prediction"
        matchStartTime.text = human_time.timestampToTime()
        matchDate.text = "${human_time.timestampToDay()} ${calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.ENGLISH
        )}, ${human_time.timestampToYear()}"
        matchPlace.text = place
        firstCommand.text = firstCommandS
        secondCommand.text = secondCommandS
        predictionResult.text = predictionResultS

        for (tip in tipsList) {
            val view = LayoutInflater.from(APP_ACTIVITY)
                .inflate(R.layout.tips_item, tips, false)
            view.findViewById<TextView>(R.id.tips_text).text = tip
            tips.addView(view)
        }

        placeABetBtn.setOnClickListener {
            YandexMetrica.reportEvent("BET")

            Amplitude.getInstance().logEvent("Place a Bet")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpenBet))
            APP_ACTIVITY.startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    companion object {
        const val PLACE_KEY = "place"
        const val NAME_KEY = "name"
        const val HUMAN_TIME_KEY = "date"
        const val TIPS_KEY = "tips"
        const val FIRST_COMMAND_KEY = "first_command"
        const val SECOND_COMMAND_KEY = "second_command"
        const val PREDICTION_RESULT_KEY = "prediction_result"

        @JvmStatic
        fun newInstance(
            name: String,
            place: String,
            human_time: String,
            tips: List<String>,
            firstCommand: String,
            secondCommand: String,
            predictionResult: String
        ) =
            MatchPredictionShowFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME_KEY, name)
                    putString(PLACE_KEY, place)
                    putString(HUMAN_TIME_KEY, human_time)
                    putString(FIRST_COMMAND_KEY, firstCommand)
                    putString(SECOND_COMMAND_KEY, secondCommand)
                    putString(PREDICTION_RESULT_KEY, predictionResult)
                    putStringArrayList(TIPS_KEY, ArrayList(tips))
                }
            }
    }
}