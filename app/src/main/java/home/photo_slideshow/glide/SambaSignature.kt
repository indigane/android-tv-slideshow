package home.photo_slideshow.glide

import com.bumptech.glide.load.Key
import java.security.MessageDigest

data class SambaSignature(private val path: String) : Key {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(path.toByteArray(Key.CHARSET))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SambaSignature
        return path == other.path
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}
