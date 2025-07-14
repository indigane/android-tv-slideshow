package home.photo_slideshow.model

import android.os.Parcelable
import com.hierynomus.smbj.share.DiskShare
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SambaFile(
    val share: @RawValue DiskShare,
    val path: String
) : Parcelable
