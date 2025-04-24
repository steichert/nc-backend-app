package za.co.newcreation.backendapp.util

import java.util.concurrent.ThreadLocalRandom

object PublicIdUtil {

    private const val MIN_PUBLIC_ID: Long = 1_000_000_000L
    private const val MAX_PUBLIC_ID: Long = 9_999_999_999L

    fun generatePublicId(): Long {
        return ThreadLocalRandom.current().nextLong(MIN_PUBLIC_ID, MAX_PUBLIC_ID)
    }
}