package home.photo_slideshow

import com.hierynomus.smbj.share.DiskShare
import home.photo_slideshow.model.SambaFile

interface ISambaRepository {
    suspend fun connect(
        server: String,
        shareName: String,
        user: String,
        pass: String,
        port: Int = 445
    ): Result<Unit>

    suspend fun fetchPhotoList(
        path: String
    ): Result<List<SambaFile>>

    fun getShare(): DiskShare?
}
