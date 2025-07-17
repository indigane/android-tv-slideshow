package home.photo_slideshow

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val serverInput: EditText = findViewById(R.id.server_input)
        val shareInput: EditText = findViewById(R.id.share_input)
        val usernameInput: EditText = findViewById(R.id.username_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val pathInput: EditText = findViewById(R.id.path_input)
        val durationInput: EditText = findViewById(R.id.duration_input)
        val progressBarSwitch: SwitchCompat = findViewById(R.id.progress_bar_switch)
        val saveButton: Button = findViewById(R.id.save_button)

        val sharedPreferences = getSharedPreferences("samba_settings", Context.MODE_PRIVATE)
        serverInput.setText(sharedPreferences.getString("server", ""))
        shareInput.setText(sharedPreferences.getString("share", ""))
        usernameInput.setText(sharedPreferences.getString("username", ""))
        passwordInput.setText(sharedPreferences.getString("password", ""))
        pathInput.setText(sharedPreferences.getString("path", ""))
        durationInput.setText(sharedPreferences.getInt("duration", 5).toString())
        progressBarSwitch.isChecked = sharedPreferences.getBoolean("show_progress_bar", true)

        saveButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("server", serverInput.text.toString())
            editor.putString("share", shareInput.text.toString())
            editor.putString("username", usernameInput.text.toString())
            editor.putString("password", passwordInput.text.toString())
            editor.putString("path", pathInput.text.toString())
            editor.putInt("duration", durationInput.text.toString().toIntOrNull() ?: 5)
            editor.putBoolean("show_progress_bar", progressBarSwitch.isChecked)
            editor.apply()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
