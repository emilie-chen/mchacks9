package dev.hongjun.sherbrooke

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AndroidUnitTest {
    @Test
    fun jsonConversion() {
        run {
            val userInfo = UserInfo(
                name = Name("First", "Last"),
                email = Email("first.last@example.com"),
                phoneNumber = PhoneNumber("123-456-7890"),
                socialNetworks = SocialNetworks(
                    discordTag = DiscordTag("tomato#0001"),
                    instagramUsername = "tomato",
                ),
                notes = "Notes"
            )
            val json = toJson(userInfo)
            val userInfoRestored = fromJson<UserInfo>(json)

            assertEquals(userInfo, userInfoRestored)
        }
    }
}