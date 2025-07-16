package home.photo_slideshow

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import home.photo_slideshow.model.SambaFile

class SlideshowActivity : AppCompatActivity() {

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private var photoFiles: ArrayList<SambaFile>? = null
    private var currentPhotoIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private var activeImageView = 1

    private val slideshowRunnable = object : Runnable {
        override fun run() {
            if (photoFiles != null && photoFiles!!.isNotEmpty()) {
                val nextPhotoIndex = (currentPhotoIndex + 1) % photoFiles!!.size

                val currentImageView = if (activeImageView == 1) imageView1 else imageView2
                val nextImageView = if (activeImageView == 1) imageView2 else imageView1

                Glide.with(this@SlideshowActivity)
                    .load(photoFiles!![nextPhotoIndex])
                    .into(nextImageView)

                currentImageView.visibility = View.GONE
                nextImageView.visibility = View.VISIBLE

                activeImageView = if (activeImageView == 1) 2 else 1
                currentPhotoIndex = nextPhotoIndex

                val nextNextPhotoIndex = (currentPhotoIndex + 1) % photoFiles!!.size
                Glide.with(this@SlideshowActivity)
                    .load(photoFiles!![nextNextPhotoIndex])
                    .preload()

                handler.postDelayed(this, 5000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slideshow)

        imageView1 = findViewById(R.id.slideshow_image_1)
        imageView2 = findViewById(R.id.slideshow_image_2)
        photoFiles = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("PHOTO_FILES", SambaFile::class.java)
        } else {
            intent.getParcelableArrayListExtra("PHOTO_FILES")
        }

        if (photoFiles != null && photoFiles!!.isNotEmpty()) {
            Glide.with(this)
                .load(photoFiles!![0])
                .into(imageView1)
            handler.postDelayed(slideshowRunnable, 5000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(slideshowRunnable)
    }
}
