package home.photo_slideshow.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import java.io.InputStream

class SambaModelLoader : ModelLoader<String, InputStream> {

    override fun buildLoadData(model: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        if (!model.startsWith("smb://")) {
            return null
        }
        return ModelLoader.LoadData(SambaSignature(model), SambaDataFetcher(model))
    }

    override fun handles(model: String): Boolean {
        return model.startsWith("smb://")
    }

    class Factory : ModelLoaderFactory<String, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            return SambaModelLoader()
        }

        override fun teardown() {
            // Do nothing
        }
    }
}
