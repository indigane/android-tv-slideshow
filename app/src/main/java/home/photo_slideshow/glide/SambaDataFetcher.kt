package home.photo_slideshow.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import home.photo_slideshow.SambaRepository
import java.io.InputStream

class SambaDataFetcher(private val model: String) : DataFetcher<InputStream> {
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val repository = SambaRepository.getInstance()
        val inputStream = repository.getInputStream(model)
        if (inputStream != null) {
            callback.onDataReady(inputStream)
        } else {
            callback.onLoadFailed(Exception("Failed to open input stream for $model"))
        }
    }

    override fun cleanup() {
        // Nothing to clean up
    }

    override fun cancel() {
        // Nothing to cancel
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }
}
