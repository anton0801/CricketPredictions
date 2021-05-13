package com.cricketpredictions.app.ui.fragments.cricket_schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cricketpredictions.app.R
import com.cricketpredictions.app.model.cricket_schedule.ScheduleSection
import com.cricketpredictions.app.utils.APP_ACTIVITY
import com.cricketpredictions.app.utils.timestampToNormalTime

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private var sections: ArrayList<ScheduleSection> = ArrayList()

    fun setSections(sections: List<ScheduleSection>) {
        this.sections.clear()
        for (section in sections) {
            if (!this.sections.contains(section)) {
                this.sections.add(section)
            }
        }
        notifyDataSetChanged()
    }

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleDate: TextView = itemView.findViewById(R.id.section_title)
        val expandBtn: ImageView = itemView.findViewById(R.id.expand_btn)
        val scheduleItems: LinearLayout = itemView.findViewById(R.id.schedule_items)

        fun bind(scheduleSection: ScheduleSection) {
            with(scheduleSection) {
                scheduleDate.text = name
                for (item in items) {
                    val view = LayoutInflater.from(APP_ACTIVITY)
                        .inflate(R.layout.schedule_inner_item, scheduleItems, false)
                    val name: TextView = view.findViewById(R.id.name)
                    val venue: TextView = view.findViewById(R.id.venue)
                    val startTime: TextView = view.findViewById(R.id.start_time)
                    val placeT: TextView = view.findViewById(R.id.place)
                    name.text = item.name
                    venue.text = item.desription
                    placeT.text = item.place
                    startTime.text = item.startTime.iso.timestampToNormalTime()
                    scheduleItems.addView(view)
                }
            }
            var isExpanded = false
            itemView.setOnClickListener {
                if (!isExpanded) {
                    expandBtn.animate().rotation(180f).duration = 200
                    scheduleItems.visibility = View.VISIBLE
                    isExpanded = true
                } else {
                    expandBtn.animate().rotation(360f).duration = 200
                    scheduleItems.visibility = View.GONE
                    isExpanded = false
                }
            }
            expandBtn.setOnClickListener {
                if (!isExpanded) {
                    expandBtn.animate().rotation(180f).duration = 200
                    scheduleItems.visibility = View.VISIBLE
                    isExpanded = true
                } else {
                    expandBtn.animate().rotation(360f).duration = 200
                    scheduleItems.visibility = View.GONE
                    isExpanded = false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun getItemCount() = sections.size

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val section = sections[position]
        holder.bind(section)
    }

}