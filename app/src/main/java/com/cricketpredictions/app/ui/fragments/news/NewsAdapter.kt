package com.cricketpredictions.app.ui.fragments.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cricketpredictions.app.R
import com.cricketpredictions.app.model.news.NewsModel
import com.cricketpredictions.app.utils.timestampToNormalTime

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var news = ArrayList<NewsModel>()
    private var listener: OnNewsClickListener? = null

    interface OnNewsClickListener {
        fun onNewsClickListener(newsModel: NewsModel)
    }

    fun setNews(news: List<NewsModel>) {
        // this.news.clear()
        for (new in news) {
            if (!this.news.contains(new)) {
                this.news.add(new)
            }
        }
        notifyDataSetChanged()
    }

    fun setOnNewsClickListener(onNewsClickListener: OnNewsClickListener) {
        listener = onNewsClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(
            view
        )
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsModel = news[position]
        holder.bind(newsModel)
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var newsTitle: TextView = itemView.findViewById(R.id.news_title)
        private var newsPublishedTime: TextView = itemView.findViewById(R.id.news_published_time)
        private var newsDescShort: TextView = itemView.findViewById(R.id.news_desc_short)

        fun bind(newsModel: NewsModel) {
            with(newsModel) {
                newsTitle.text = title
                newsPublishedTime.text = publishedDate.timestampToNormalTime()
                newsDescShort.text = description_text
            }
            itemView.setOnClickListener {
                listener?.onNewsClickListener(newsModel)
            }
        }
    }

}