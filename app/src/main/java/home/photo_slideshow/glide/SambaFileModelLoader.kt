package home.photo_slideshow.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import home.photo_slideshow.model.SambaFile
import java.io.InputStream

import home.photo_slideshow.ISambaRepository
import home.photo_slideshow.SambaRepository

class SambaFileModelLoader(private val repository: ISambaRepository) : ModelLoader<SambaFile, InputStream> {

    override fun buildLoadData(
        model: SambaFile,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model.path), SambaFileDataFetcher(model, repository))
    }

    override fun handles(model: SambaFile): Boolean {
        return true
    }

    class Factory : ModelLoaderFactory<SambaFile, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<SambaFile, InputStream> {
            return SambaFileModelLoader(SambaRepository.getInstance())
        }

        override fun teardown() {
            // Do nothing
        }
    }
}
