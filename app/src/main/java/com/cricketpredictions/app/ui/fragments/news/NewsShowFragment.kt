package com.cricketpredictions.app.ui.fragments.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.amplitude.api.Amplitude
import com.cricketpredictions.app.R
import com.cricketpredictions.app.model.news.Provider
import com.cricketpredictions.app.ui.fragments.match_predictions.MatchPredictionsFragment
import com.cricketpredictions.app.utils.APP_ACTIVITY
import com.cricketpredictions.app.utils.isShowingAds
import com.cricketpredictions.app.utils.loadAndSetImage
import com.cricketpredictions.app.utils.replaceFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.button.MaterialButton
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class NewsShowFragment : Fragment() {

    private var title = ""
    private var description = ""
    private var descriptionText = ""
    private var provider = Provider()

    private lateinit var backBtn: ImageView
    private lateinit var newsImage: ImageView
    private lateinit var newsTitle: TextView
    private lateinit var newsProviderName: TextView
    private lateinit var newsDescription: TextView
    private lateinit var openMatchPredictionsBtn: MaterialButton
    private lateinit var openFullArticleBtn: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = arguments?.getString(TITLE_KEY) ?: ""
        description = arguments?.getString(DESCRIPTION_KEY) ?: ""
        descriptionText = arguments?.getString(DESCRIPTION_TEXT_KEY) ?: ""
        provider = Provider(
            arguments?.getString(PROVIDER_NAME_KEY) ?: "",
            arguments?.getString(PROVIDER_URL_KEY) ?: ""
        )

        with(view) {
            backBtn = findViewById(R.id.back_btn)
            newsImage = findViewById(R.id.news_image)
            newsTitle = findViewById(R.id.news_title)
            newsProviderName = findViewById(R.id.news_provider_name)
            newsDescription = findViewById(R.id.news_description)
            openMatchPredictionsBtn = findViewById(R.id.open_match_predictions_btn)
            openFullArticleBtn = findViewById(R.id.open_full_article_btn)
        }

        backBtn.setOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        newsTitle.text = title

        newsProviderName.text = provider.name
        newsDescription.text = descriptionText

        val amplitude = Amplitude.getInstance()
        openMatchPredictionsBtn.setOnClickListener {
            amplitude.logEvent("NewsToMatch")
            APP_ACTIVITY.supportFragmentManager.popBackStack()
            replaceFragment(MatchPredictionsFragment())
            APP_ACTIVITY.bottomNavigationView.selectedItemId = R.id.nav_match_predictions
        }
        openFullArticleBtn.setOnClickListener {
            amplitude.logEvent("Read Full Article")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(provider.url))
            startActivity(intent)
        }

        val link = getUrlToImage(description)
        if (link.isEmpty()) {
            newsImage.visibility = View.GONE
        } else {
            newsImage.loadAndSetImage(link)
        }
    }

    private fun getUrlToImage(html: String): String {
        var link = ""
        val srcs: Elements = Jsoup.parse(html).getElementsByTag("img")
        for (i in 0 until srcs.size) {
            link = srcs[i].attr("src")
            break
        }
        return link
    }

    companion object {
        const val TITLE_KEY = "title"
        const val DESCRIPTION_KEY = "description"
        const val DESCRIPTION_TEXT_KEY = "description_text"
        const val PROVIDER_NAME_KEY = "provider_name"
        const val PROVIDER_URL_KEY = "provider_url"

        fun newInstance(
            title: String,
            description: String,
            descriptionText: String,
            provider: Provider = Provider()
        ): NewsShowFragment {
            val args = Bundle()
            args.putString(TITLE_KEY, title)
            args.putString(DESCRIPTION_KEY, description)
            args.putString(DESCRIPTION_TEXT_KEY, descriptionText)
            args.putString(PROVIDER_NAME_KEY, provider.name)
            args.putString(PROVIDER_URL_KEY, provider.url)
            val fragment = NewsShowFragment()
            fragment.arguments = args
            return fragment
        }
    }

}