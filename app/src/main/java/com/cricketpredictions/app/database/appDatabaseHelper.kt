package com.cricketpredictions.app.database

import android.util.Log
import com.cricketpredictions.app.model.ShowingAds
import com.cricketpredictions.app.model.match_prediction.MatchPrediction
import com.cricketpredictions.app.utils.isShowingAds
import com.google.firebase.database.*

lateinit var REF_DATABASE_ROOT: DatabaseReference

const val MATCH_PREDICTIONS_NODE = "predictions"

const val CHILD_TITLE = "title"
const val CHILD_DATE = "title"
const val CHILD_TIME = "title"
const val CHILD_PLACE = "title"
const val CHILD_URL_TO_POST = "title"
const val CHILD_ICONS = "title"

fun initFirebase() {
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
}

fun DataSnapshot.getPredictionModel() =
    this.getValue(MatchPrediction::class.java) ?: MatchPrediction()

fun isShowAdAvailable(onSuccess: () -> Unit) {
    REF_DATABASE_ROOT.child("showing_ads")
        .child("isShow")
        .addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d("FIREBASE_TEST", "onCancelled: ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                isShowingAds = ShowingAds(snapshot.value.toString().toBoolean())
                Log.d("FIREBASE_TEST", "onDataChange: $isShowingAds")
                onSuccess()
            }
        })
}