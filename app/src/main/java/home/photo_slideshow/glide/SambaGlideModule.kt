package home.photo_slideshow.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import home.photo_slideshow.model.SambaFile
import java.io.InputStream

@GlideModule
class SambaGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(SambaFile::class.java, InputStream::class.java, SambaFileModelLoader.Factory())
    }
}
