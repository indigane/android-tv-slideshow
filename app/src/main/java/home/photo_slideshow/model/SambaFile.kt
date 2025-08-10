package home.photo_slideshow.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SambaFile(
    val path: String,
    val lastModified: Long
) : Parcelable
