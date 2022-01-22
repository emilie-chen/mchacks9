package dev.hongjun.sherbrooke

import android.text.TextUtils
import android.util.Patterns
import java.lang.Character.isDigit

fun isValidEmail(target: String): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

@JvmInline
value class Email(private val value: String) {
    init {
        require(isValidEmail(value))
    }

    override fun toString(): String {
        return value
    }
}

data class Name(val first: String, val last: String) {
    init {
        require(first.isNotEmpty())
        require(last.isNotEmpty())
        require(!first.contains(" "))
        require(!last.contains(" "))
    }

    override fun toString(): String {
        return "$first $last"
    }
}

fun requireNorthAmericanPhoneNumber(value: String) {
    // 000-000-0000
    require(value.length == 12)
    value.forEachIndexed { i, digit ->
        when (i) {
            3, 7 -> require(digit == '-')
            else -> require(isDigit(digit))
        }
    }
}

fun formatNorthAmericanPhoneNumber(value: String): String {
    // 000,000,0000 to 000-000-0000
    require(value.length == 10)
    value.forEach {
        require(isDigit(it))
    }

    val arr = CharArray(12)
    for (i in 0..2) {
        arr[i] = value[i]
    }
    arr[3] = '-'
    for (i in 4..6) {
        arr[i] = value[i - 1]
    }
    arr[7] = '-'
    for (i in 8..11) {
        arr[i] = value[i - 2]
    }

    return String(arr)
}

@JvmInline
value class PhoneNumber(private val value: String) {
    init {
        requireNorthAmericanPhoneNumber(value)
    }

    override fun toString(): String {
        return value
    }
}

@JvmInline
value class DiscordTag(private val value: String) {
    init {
        // _____...___#XXXX
        require(value.length >= 6)
        value.forEachIndexed { index, c ->
            when (index) {
                value.length - 5 -> require(c == '#')
                in (value.length - 5) until value.length -> require(isDigit(c))
            }
        }

        value.forEachIndexed { index, c ->
            when (index) {
                value.length - 5 -> require(c == '#')
                in (value.length - 5) until value.length -> require(isDigit(c))
            }
        }

        require(discriminator.toInt() > 0)
    }

    val username: String
        get() = value.split("#")[0]

    val discriminator: String
        get() = value.split("#")[1]

    override fun toString(): String {
        return value
    }
}

data class SocialNetworks(val discordTag: DiscordTag?, val instagramUsername: String?)

data class UserInfo(
    val name: String,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val socialNetworks: SocialNetworks,
    val notes: String = ""
)
