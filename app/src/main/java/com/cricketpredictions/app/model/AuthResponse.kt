package com.cricketpredictions.app.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("auth")
    var auth: Auth = Auth("")
) {

    data class Auth(
        @SerializedName("access_token")
        var accessToken: String = ""
    )

}