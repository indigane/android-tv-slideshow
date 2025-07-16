package home.photo_slideshow

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import home.photo_slideshow.model.SambaFile

class SlideshowActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var photoFiles: ArrayList<SambaFile>? = null
    private var currentPhotoIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private val slideshowRunnable = object : Runnable {
        override fun run() {
            if (photoFiles != null && photoFiles!!.isNotEmpty()) {
                currentPhotoIndex = (currentPhotoIndex + 1) % photoFiles!!.size
                Glide.with(this@SlideshowActivity)
                    .load(photoFiles!![currentPhotoIndex])
                    .into(imageView)
                handler.postDelayed(this, 5000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slideshow)

        imageView = findViewById(R.id.slideshow_image)
        photoFiles = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("PHOTO_FILES", SambaFile::class.java)
        } else {
            intent.getParcelableArrayListExtra("PHOTO_FILES")
        }

        if (photoFiles != null && photoFiles!!.isNotEmpty()) {
            Glide.with(this)
                .load(photoFiles!![0])
                .into(imageView)
            handler.postDelayed(slideshowRunnable, 5000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(slideshowRunnable)
    }
}
