package home.photo_slideshow

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide

class SlideshowActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slideshow)

        val imageView: ImageView = findViewById(R.id.slideshow_image)
        val photoPaths = intent.getStringArrayListExtra("PHOTO_PATHS")

        if (photoPaths != null && photoPaths.isNotEmpty()) {
            Glide.with(this)
                .load(photoPaths[0])
                .into(imageView)
        }
    }
}
