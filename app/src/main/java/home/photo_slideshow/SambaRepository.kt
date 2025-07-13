package home.photo_slideshow

import android.util.Log
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SambaRepository {

    suspend fun fetchPhotoList(
        server: String,
        share: String,
        user: String,
        pass: String,
        callback: (Result<List<String>>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val client = SMBClient()
                val connection: Connection = client.connect(server)
                val ac = AuthenticationContext(user, pass.toCharArray(), null)
                val session: Session = connection.authenticate(ac)
                val share = session.connectShare(share) as DiskShare

                val photoList = mutableListOf<String>()
                for (fileId in share.list("", "*.*")) {
                    val fileName = fileId.fileName
                    if (fileName.endsWith(".jpg", true) || fileName.endsWith(".png", true)) {
                        photoList.add(fileName)
                    }
                }
                callback(Result.success(photoList))
            } catch (e: Exception) {
                Log.e("SambaRepository", "Error fetching photo list", e)
                callback(Result.failure(e))
            }
        }
    }
}
