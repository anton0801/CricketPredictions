package com.cricketpredictions.app.ui.fragments.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cricketpredictions.app.R
import com.cricketpredictions.app.model.news.NewsDataResponse
import com.cricketpredictions.app.model.news.NewsModel
import com.cricketpredictions.app.utils.*
import com.amplitude.api.Amplitude
import com.cricketpredictions.app.INTERSTITIAL_AD_ID
import com.cricketpredictions.app.ui.fragments.splash.ACCESS_TOKEN_KEY
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class NewsFragment : Fragment(), NewsAdapter.OnNewsClickListener {

    private var mInterstitialAd: InterstitialAd? = null

    private var news = ArrayList<NewsModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        APP_ACTIVITY.mainToolbar.title = "News"

        with(view) {
            recyclerView = findViewById(R.id.news_recycler_view)
            progressBar = findViewById(R.id.progress_bar)
        }

        recyclerView.apply {
            newsAdapter =
                NewsAdapter()
            newsAdapter.setNews(news)
            newsAdapter.setOnNewsClickListener(this@NewsFragment)
            adapter = newsAdapter
            linearLayoutManager = LinearLayoutManager(APP_ACTIVITY)
            layoutManager = linearLayoutManager
        }

        getNews()
    }

    private fun getNews() {
        progressBar.visibility = View.VISIBLE
        APP.getApi().getNews(SHARED_MANAGER.getString(ACCESS_TOKEN_KEY)!!)
            .enqueue(AppRetrofitCallback<NewsDataResponse> {
                val response = it.body()
                if (response != null) {
                    news.addAll(response.data.news)
                    newsAdapter.setNews(news)
                    progressBar.visibility = View.GONE
                }
            })
    }

    override fun onNewsClickListener(newsModel: NewsModel) {
        Amplitude.getInstance().logEvent("News Open")

        if (isShowingAds.isShow) {
            progressBar.visibility = View.VISIBLE
            InterstitialAd.load(
                APP_ACTIVITY,
                INTERSTITIAL_AD_ID,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d("ADS_LOAD_ERROR", p0.message)
                        replaceFragment(
                            NewsShowFragment.newInstance(
                                newsModel.title,
                                newsModel.description,
                                newsModel.description_text,
                                newsModel.provider
                            ),
                            true
                        )
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        progressBar.visibility = View.GONE
                        mInterstitialAd = p0
                        if (mInterstitialAd != null) {
                            mInterstitialAd!!.show(APP_ACTIVITY)
                        }
                        replaceFragment(
                            NewsShowFragment.newInstance(
                                newsModel.title,
                                newsModel.description,
                                newsModel.description_text,
                                newsModel.provider
                            ),
                            true
                        )
                    }
                })
        } else {
            replaceFragment(
                NewsShowFragment.newInstance(
                    newsModel.title,
                    newsModel.description,
                    newsModel.description_text,
                    newsModel.provider
                ),
                true
            )
        }
    }

}