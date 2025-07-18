package home.photo_slideshow

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlideListener(
    private val onResourceReady: () -> Unit,
    private val onLoadFailed: () -> Unit,
    private val latch: java.util.concurrent.CountDownLatch? = null
) : RequestListener<Drawable> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {
        onLoadFailed()
        latch?.countDown()
        return false
    }

    override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: Target<Drawable>,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        onResourceReady()
        latch?.countDown()
        return false
    }
}
