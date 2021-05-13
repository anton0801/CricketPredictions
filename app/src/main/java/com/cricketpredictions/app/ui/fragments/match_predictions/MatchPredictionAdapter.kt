package com.cricketpredictions.app.ui.fragments.match_predictions

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cricketpredictions.app.R
import com.cricketpredictions.app.model.match_prediction.MatchPrediction
import com.cricketpredictions.app.model.match_prediction.SectionItem
import com.cricketpredictions.app.utils.*
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.collections.ArrayList

class SectionsAdapter(val listener: MatchPredictionAdapter.OnReadBtnClickedListener) :
    RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    private var sections: ArrayList<SectionItem> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectionTitle: TextView = itemView.findViewById(R.id.section_title)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.child_recycler_view)

        fun bind(sectionItem: SectionItem) {
            sectionTitle.text = sectionItem.name
            childRecyclerView.apply {
                adapter = MatchPredictionAdapter().apply {
                    setMatchs(sectionItem.items)
                    setOnReadBtnClickedListener(listener)
                }
                layoutManager = LinearLayoutManager(APP_ACTIVITY)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.section_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = sections.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
        holder.bind(section)
    }

}

class MatchPredictionAdapter :
    RecyclerView.Adapter<MatchPredictionAdapter.MatchPredictionViewHolder>() {

    private var matcs = ArrayList<MatchPrediction>()
    private var listener: OnReadBtnClickedListener? = null

    interface OnReadBtnClickedListener {
        fun onReadBtnClicked(matchPrediction: MatchPrediction)
    }

    fun setMatchs(machs: List<MatchPrediction>) {
        this.matcs.clear()
        this.matcs.addAll(machs)
        notifyDataSetChanged()
    }

    fun setOnReadBtnClickedListener(onReadBtnClickedListener: OnReadBtnClickedListener) {
        listener = onReadBtnClickedListener
    }

    inner class MatchPredictionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var matchName: TextView = itemView.findViewById(R.id.match_name)
        private var expandBtn: ImageView = itemView.findViewById(R.id.expand_btn)
        private var matchContent: LinearLayout = itemView.findViewById(R.id.match_content)
        private var matchStartTime: TextView = itemView.findViewById(R.id.match_start_time)
        private var dateOfMatch: TextView = itemView.findViewById(R.id.date_of_match)
        private var matchPlace: TextView = itemView.findViewById(R.id.match_place)
        private var readMoreBtn: MaterialButton = itemView.findViewById(R.id.read_more_btn)
        private var logoFirstCommand: ImageView = itemView.findViewById(R.id.logo_first_command)
        private var logoSecondCommand: ImageView = itemView.findViewById(R.id.logo_second_command)

        fun bind(matchPrediction: MatchPrediction) {
            val calendar = Calendar.getInstance()
            with(matchPrediction) {
                calendar.set(Calendar.MONTH, human_time.timestampToMonth().toInt() - 1)

                matchName.text =
                    if (name.isNotEmpty()) name else "${team_a.full_name} vs ${team_b.full_name} Match Prediction"
                matchStartTime.text = human_time.timestampToTime()
                dateOfMatch.text = "${human_time.timestampToDay()} ${calendar.getDisplayName(
                    Calendar.MONTH,
                    Calendar.LONG,
                    Locale.ENGLISH
                )}, ${human_time.timestampToYear()}"
                matchPlace.text = place
                logoFirstCommand.loadAndSetImage(team_a.image)
                logoSecondCommand.loadAndSetImage(team_b.image)
            }
            var isExpanded = true
            expandBtn.setOnClickListener {
                if (!isExpanded) {
                    expandBtn.animate().rotation(180f).duration = 200
                    matchContent.visibility = View.VISIBLE
                    isExpanded = true
                } else {
                    expandBtn.animate().rotation(360f).duration = 200
                    matchContent.visibility = View.GONE
                    isExpanded = false
                }
            }
            itemView.setOnClickListener {
                if (!isExpanded) {
                    expandBtn.animate().rotation(180f).duration = 200
                    matchContent.visibility = View.VISIBLE
                    isExpanded = true
                } else {
                    expandBtn.animate().rotation(360f).duration = 200
                    matchContent.visibility = View.GONE
                    isExpanded = false
                }
            }
            readMoreBtn.setOnClickListener {
                listener?.onReadBtnClicked(matchPrediction)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchPredictionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_predictions_item, parent, false)
        return MatchPredictionViewHolder(view)
    }

    override fun getItemCount() = matcs.size

    override fun onBindViewHolder(holder: MatchPredictionViewHolder, position: Int) {
        val match = matcs[position]
        holder.bind(match)
    }

}