package home.photo_slideshow.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import home.photo_slideshow.model.SambaFile
import java.io.InputStream

class SambaFileDataFetcher(private val model: SambaFile) : DataFetcher<InputStream> {

    private var stream: InputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        try {
            val file = model.share.openFile(
                model.path,
                setOf(com.hierynomus.msdtyp.AccessMask.GENERIC_READ),
                null,
                setOf(com.hierynomus.mssmb2.SMB2ShareAccess.FILE_SHARE_READ),
                com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_OPEN,
                null
            )
            stream = file.inputStream
            callback.onDataReady(stream)
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }
    }

    override fun cleanup() {
        try {
            stream?.close()
        } catch (e: Exception) {
            // Ignore
        }
    }

    override fun cancel() {
        cleanup()
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }
}
