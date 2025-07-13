package com.example.sambavideoplayer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serverInput: EditText = findViewById(R.id.server_input)
        val shareInput: EditText = findViewById(R.id.share_input)
        val usernameInput: EditText = findViewById(R.id.username_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val connectButton: Button = findViewById(R.id.connect_button)

        connectButton.setOnClickListener {
            val server = serverInput.text.toString()
            val share = shareInput.text.toString()
            val user = usernameInput.text.toString()
            val pass = passwordInput.text.toString()

            val repository = SambaRepository()
            CoroutineScope(Dispatchers.Main).launch {
                repository.fetchPhotoList(server, share, user, pass) { result ->
                    result.onSuccess { photoList ->
                        Log.d("SambaApp", "Success! Photos found: ${photoList.size}")
                    }.onFailure { error ->
                        Log.e("SambaApp", "Error fetching photos", error)
                    }
                }
            }
        }
    }
}
