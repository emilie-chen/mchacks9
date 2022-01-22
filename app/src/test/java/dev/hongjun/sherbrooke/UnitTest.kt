package dev.hongjun.sherbrooke

import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException

inline fun <reified E : Throwable> assertException(block: () -> Unit) {
    try {
        block()
        assert(false)
    } catch (e: Throwable) {
        if (e is E) {
            assert(true)
        } else {
            assert(false)
        }
    }
}

class UnitTest {
    @Test
    fun userInfoValidation() {
        run {
            PhoneNumber("123-456-7890")
        }

        assertException<IllegalArgumentException> {
            PhoneNumber("123-456-789")
        }

        assertException<IllegalArgumentException> {
            PhoneNumber("123-456-789a")
        }

        run {
            assertEquals(formatNorthAmericanPhoneNumber("1234567890"),
                PhoneNumber("123-456-7890").toString())
        }

        assertException<IllegalArgumentException> {
            PhoneNumber("123-456-789")
        }

        run {
            DiscordTag("tomato#1234")
            DiscordTag("Hello World#5223")
        }

        assertException<IllegalArgumentException> {
            DiscordTag("#1234")
        }

        assertException<IllegalArgumentException> {
            DiscordTag("tomato#123")
        }

        assertException<IllegalArgumentException> {
            DiscordTag("tomato#123a")
        }

        assertException<IllegalArgumentException> {
            DiscordTag("tomato#0000")
        }
    }
}