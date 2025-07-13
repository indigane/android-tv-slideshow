package home.photo_slideshow

import android.util.Log
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class SambaRepository {

    private var connection: Connection? = null
    private var session: Session? = null
    private var share: DiskShare? = null

    companion object {
        @Volatile
        private var INSTANCE: SambaRepository? = null

        fun getInstance(): SambaRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SambaRepository().also { INSTANCE = it }
            }
    }

    suspend fun connect(
        server: String,
        shareName: String,
        user: String,
        pass: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val client = SMBClient()
                connection = client.connect(server)
                val ac = AuthenticationContext(user, pass.toCharArray(), null)
                session = connection?.authenticate(ac)
                share = session?.connectShare(shareName) as DiskShare
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("SambaRepository", "Error connecting to share", e)
                Result.failure(e)
            }
        }
    }

    suspend fun fetchPhotoList(
        path: String
    ): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val photoList = mutableListOf<String>()
                val root = if (path.isEmpty()) "" else path
                recursiveSearch(root, photoList)
                Result.success(photoList)
            } catch (e: Exception) {
                Log.e("SambaRepository", "Error fetching photo list", e)
                Result.failure(e)
            }
        }
    }

    private fun recursiveSearch(path: String, photoList: MutableList<String>) {
        share?.let {
            for (fileId in it.list(path)) {
                val fileName = fileId.fileName
                val newPath = if (path.isEmpty()) fileName else "$path/$fileName"
                if (fileId.fileAttributes and 16L > 0) { // DIRECTORY
                    if (fileName != "." && fileName != "..") {
                        recursiveSearch(newPath, photoList)
                    }
                } else {
                    if (fileName.endsWith(".jpg", true) || fileName.endsWith(".png", true)) {
                        photoList.add(newPath)
                    }
                }
            }
        }
    }

    fun getInputStream(path: String): InputStream? {
        return try {
            val file = share?.openFile(
                path,
                setOf(com.hierynomus.msdtyp.AccessMask.GENERIC_READ),
                null,
                setOf(com.hierynomus.mssmb2.SMB2ShareAccess.FILE_SHARE_READ),
                com.hierynomus.mssmb2.SMB2CreateDisposition.FILE_OPEN,
                null
            )
            file?.inputStream
        } catch (e: Exception) {
            Log.e("SambaRepository", "Error getting input stream", e)
            null
        }
    }
}
