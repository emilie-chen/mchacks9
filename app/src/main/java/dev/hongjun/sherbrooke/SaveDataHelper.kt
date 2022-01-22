package dev.hongjun.sherbrooke

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager.*

fun getUserProfile(context: Context) =
    getDefaultSharedPreferences(context).getString("user_profile", "")?.let {
        if (it.isEmpty()) {
            null
        } else {
            fromJson<UserInfo>(it)
        }
    }

fun setUserProfile(context: Context, userInfo: UserInfo) =
    getDefaultSharedPreferences(context).edit {
        putString("user_profile", toJson(userInfo))
    }
