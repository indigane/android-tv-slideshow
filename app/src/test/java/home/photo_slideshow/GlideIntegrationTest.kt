package home.photo_slideshow

import com.bumptech.glide.load.Options
import home.photo_slideshow.glide.SambaFileDataFetcher
import home.photo_slideshow.glide.SambaFileModelLoader
import home.photo_slideshow.model.SambaFile
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GlideIntegrationTest {

    private lateinit var modelLoader: SambaFileModelLoader
    private lateinit var mockRepository: ISambaRepository

    @Before
    fun setUp() {
        mockRepository = Mockito.mock(ISambaRepository::class.java)
        modelLoader = SambaFileModelLoader(mockRepository)
    }

    @Test
    fun testBuildLoadData() {
        // Given
        val sambaFile = SambaFile("test.jpg")
        val options = Options()

        // When
        val loadData = modelLoader.buildLoadData(sambaFile, 100, 100, options)

        // Then
        assertEquals(sambaFile.path, (loadData?.fetcher as SambaFileDataFetcher).model.path)
        assertEquals(mockRepository, (loadData.fetcher as SambaFileDataFetcher).repository)
    }
}
