package home.photo_slideshow.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import home.photo_slideshow.model.SambaFile
import java.io.InputStream

class SambaFileModelLoader : ModelLoader<SambaFile, InputStream> {

    override fun buildLoadData(
        model: SambaFile,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model.path), SambaFileDataFetcher(model))
    }

    override fun handles(model: SambaFile): Boolean {
        return true
    }

    class Factory : ModelLoaderFactory<SambaFile, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<SambaFile, InputStream> {
            return SambaFileModelLoader()
        }

        override fun teardown() {
            // Do nothing
        }
    }
}
