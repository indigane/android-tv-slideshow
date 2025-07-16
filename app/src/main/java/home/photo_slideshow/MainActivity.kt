package home.photo_slideshow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectButton: Button = findViewById(R.id.connect_button)

        connectButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("samba_settings", Context.MODE_PRIVATE)
            val server = sharedPreferences.getString("server", "") ?: ""
            val share = sharedPreferences.getString("share", "") ?: ""
            val user = sharedPreferences.getString("username", "") ?: ""
            val pass = sharedPreferences.getString("password", "") ?: ""
            val path = sharedPreferences.getString("path", "") ?: ""

            if (server.isEmpty() || share.isEmpty()) {
                startActivity(Intent(this, SettingsActivity::class.java))
                return@setOnClickListener
            }

            val repository = SambaRepository.getInstance()
            CoroutineScope(Dispatchers.Main).launch {
                repository.connect(server, share, user, pass).onSuccess {
                    repository.fetchPhotoList(path).onSuccess { photoList ->
                        Log.d("SambaApp", "Success! Photos found: ${photoList.size}")
                        val intent = Intent(this@MainActivity, SlideshowActivity::class.java).apply {
                            putParcelableArrayListExtra("PHOTO_FILES", ArrayList(photoList))
                        }
                        startActivity(intent)
                    }.onFailure { error ->
                        Log.e("SambaApp", "Error fetching photos", error)
                    }
                }.onFailure { error ->
                    Log.e("SambaApp", "Error connecting to share", error)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
