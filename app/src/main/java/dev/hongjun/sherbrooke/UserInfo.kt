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
    value.forEachIndexed {
        i, digit -> when(i) {
            3, 7 -> require(digit == '-')
            else -> require(isDigit(digit))
        }
    }
}

fun formatNorthAmericanPhoneNumber(value: String) {
    // 000,000,0000 to 000-000-0000
    require(value.length == 10)
    value.forEach {
        require(isDigit(it))
    }


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

data class UserInfo(val name: String, val email: Email)
