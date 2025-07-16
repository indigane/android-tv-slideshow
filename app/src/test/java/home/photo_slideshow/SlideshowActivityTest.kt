package home.photo_slideshow

import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import home.photo_slideshow.model.SambaFile
import org.junit.Before
import org.junit.Test
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows
import home.photo_slideshow.R
import android.app.Application
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication
import org.robolectric.shadows.ShadowLooper
import java.time.Duration
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28], application = Application::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SlideshowActivityTest {

    private lateinit var scenario: ActivityScenario<SlideshowActivity>
    private lateinit var activity: SlideshowActivity
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView

    @Before
    fun setUp() {
        activity = SlideshowActivity()
    }

    @Test
    fun testSlideshowSwapsImageViews() {

        val photos = arrayListOf(
            SambaFile("smb://server/share/photo1.jpg"),
            SambaFile("smb://server/share/photo2.jpg")
        )
        activity.intent.putParcelableArrayListExtra("PHOTO_FILES", photos)

        // Initially, imageView1 is visible and imageView2 is gone
        assert(imageView1.visibility == View.VISIBLE)
        assert(imageView2.visibility == View.GONE)
    }
}
