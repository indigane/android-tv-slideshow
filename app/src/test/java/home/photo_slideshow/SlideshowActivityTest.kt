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
import android.content.Context
import android.widget.ProgressBar

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28], application = Application::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SlideshowActivityTest {

    private lateinit var scenario: ActivityScenario<SlideshowActivity>
    private lateinit var progressBar: ProgressBar

    @Test
    fun testProgressBarIsVisibleByDefault() {
        val photos = arrayListOf(SambaFile("smb://server/share/photo1.jpg"))
        val intent = ApplicationProvider.getApplicationContext<Context>().let {
            android.content.Intent(it, SlideshowActivity::class.java).apply {
                putParcelableArrayListExtra("PHOTO_FILES", photos)
            }
        }

        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity ->
            progressBar = activity.findViewById(R.id.slideshow_progress_bar)
            assert(progressBar.visibility == View.VISIBLE)
        }
    }

    @Test
    fun testProgressBarIsHiddenWhenSettingIsDisabled() {
        val photos = arrayListOf(SambaFile("smb://server/share/photo1.jpg"))
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sharedPreferences = context.getSharedPreferences("samba_settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("show_progress_bar", false)
            commit()
        }

        val intent = context.let {
            android.content.Intent(it, SlideshowActivity::class.java).apply {
                putParcelableArrayListExtra("PHOTO_FILES", photos)
            }
        }

        scenario = ActivityScenario.launch(intent)
        scenario.onActivity { activity ->
            progressBar = activity.findViewById(R.id.slideshow_progress_bar)
            assert(progressBar.visibility == View.GONE)
        }
    }
}
