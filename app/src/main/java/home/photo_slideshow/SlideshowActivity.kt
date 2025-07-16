package home.photo_slideshow

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import home.photo_slideshow.model.SambaFile

class SlideshowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slideshow)

        val imageView: ImageView = findViewById(R.id.slideshow_image)
        val photoFiles = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("PHOTO_FILES", SambaFile::class.java)
        } else {
            intent.getParcelableArrayListExtra("PHOTO_FILES")
        }


        if (photoFiles != null && photoFiles.isNotEmpty()) {
            Glide.with(this)
                .load(photoFiles[0])
                .into(imageView)
        }
    }
}
