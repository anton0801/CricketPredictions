package com.cricketpredictions.app.ui.fragments.cricket_schedule

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
import com.cricketpredictions.app.model.cricket_schedule.ScheduleDataResponse
import com.cricketpredictions.app.model.cricket_schedule.ScheduleItem
import com.cricketpredictions.app.model.cricket_schedule.ScheduleSection
import com.cricketpredictions.app.model.match_prediction.MatchPrediction
import com.cricketpredictions.app.ui.fragments.splash.ACCESS_TOKEN_KEY
import com.cricketpredictions.app.utils.*
import java.util.*
import kotlin.collections.ArrayList

class CriketScheduleFragment : Fragment() {

    private var sections: ArrayList<ScheduleSection> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        APP_ACTIVITY.mainToolbar.title = "Schedule"
        return inflater.inflate(R.layout.fragment_criket_schedul, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            recyclerView = findViewById(R.id.recycler_view_schedule)
            progressBar = findViewById(R.id.progress_bar)
        }

        recyclerView.apply {
            scheduleAdapter = ScheduleAdapter()
            layoutManager = LinearLayoutManager(APP_ACTIVITY)
            adapter = scheduleAdapter
        }

        showCricketSchedule()
    }

    private fun showCricketSchedule() {
        progressBar.visibility = View.VISIBLE
        APP.getApi().getCricketSchedule(SHARED_MANAGER.getString(ACCESS_TOKEN_KEY)!!)
            .enqueue(AppRetrofitCallback<ScheduleDataResponse> {
                val scheduleDataResponse = it.body()
                if (scheduleDataResponse != null) {
                    addScheduleItems(scheduleDataResponse) {
//                        APP.getApi()
//                            .getCricketSchedule(
//                                SHARED_MANAGER.getString(ACCESS_TOKEN_KEY)!!,
//                                scheduleDataResponse.data.nextMonth
//                            )
//                            .enqueue(AppRetrofitCallback<ScheduleDataResponse> { task ->
//                                val scheduleDataResponse2 = task.body()
//                                if (scheduleDataResponse2 != null) {
//                                    if (scheduleDataResponse2.data != null) {
//                                        addScheduleItems(scheduleDataResponse) { }
//                                    }
//                                }
//                            })
                        scheduleAdapter.setSections(sections)
                    }
                }
                progressBar.visibility = View.GONE
            })
    }

    private fun addScheduleItems(
        scheduleDataResponse: ScheduleDataResponse,
        onSuccess: () -> Unit
    ) {
        val scheduleDays = scheduleDataResponse.data.months[0].days
        val calendar = Calendar.getInstance()
        for (scheduleDay in scheduleDays) {
            if (scheduleDay.day >= calendar.get(Calendar.DAY_OF_MONTH)) {
                if (scheduleDay.matches.isNotEmpty()) {
                    calendar.set(Calendar.DAY_OF_MONTH, scheduleDay.day)
                    val matches = ArrayList<ScheduleItem>()
                    for (match in scheduleDay.matches) {
                        if (!matches.contains(match)) {
                            matches.add(match)
                        }
                    }
                    sections.add(
                        ScheduleSection(
                            "${calendar.getDisplayName(
                                Calendar.DAY_OF_WEEK,
                                Calendar.SHORT,
                                Locale.ENGLISH
                            )}, ${calendar.getDisplayName(
                                Calendar.MONTH,
                                Calendar.LONG,
                                Locale.ENGLISH
                            )} ${scheduleDay.day} ${calendar.get(
                                Calendar.YEAR
                            )}",
                            matches
                        )
                    )
                }
            }
        }
        for (section in sections) {
            val prevItem = ScheduleItem()
            for ((i, match) in section.items.withIndex()) {
                if (match == prevItem) {
                    (section.items as ArrayList).removeAt(i)
                }
            }
        }
        onSuccess()
    }

}