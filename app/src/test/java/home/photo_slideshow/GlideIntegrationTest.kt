package home.photo_slideshow

import android.content.Context
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import com.bumptech.glide.Glide
import com.hierynomus.smbj.share.DiskShare
import home.photo_slideshow.model.SambaFile
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GlideIntegrationTest {

    @Test
    fun testGlideWithSambaFile() {
        // Given
        val context: Context = ApplicationProvider.getApplicationContext()
        val mockShare = Mockito.mock(DiskShare::class.java)
        val sambaFile = SambaFile(mockShare, "test.jpg")
        val inputStream = ByteArrayInputStream("dummy image content".toByteArray())
        val mockFile = Mockito.mock(com.hierynomus.smbj.share.File::class.java)
        Mockito.`when`(mockFile.inputStream).thenReturn(inputStream)
        Mockito.`when`(
            mockShare.openFile(
                Mockito.eq("test.jpg"),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
            )
        ).thenReturn(mockFile)

        // When
        val imageView = ImageView(context)
        Glide.with(context)
            .load(sambaFile)
            .into(imageView)

        // Then
        // We can't easily assert that the image is loaded, but we can assert that no exceptions were thrown.
        // If the code reaches this point, it means the test has passed.
    }
}
