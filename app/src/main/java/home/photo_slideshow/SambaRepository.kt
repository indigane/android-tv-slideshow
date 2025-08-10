package home.photo_slideshow

import android.util.Log
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import home.photo_slideshow.model.SambaFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class SambaRepository : ISambaRepository {

    private var connection: Connection? = null
    private var session: Session? = null
    private var share: DiskShare? = null

    companion object {
        @Volatile
        private var INSTANCE: ISambaRepository? = null

        fun getInstance(): ISambaRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SambaRepository().also { INSTANCE = it }
            }
    }

    override suspend fun connect(
        server: String,
        shareName: String,
        user: String,
        pass: String,
        port: Int
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val client = SMBClient()
                connection = client.connect(server, port)
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

    override suspend fun fetchPhotoList(
        path: String
    ): Result<List<SambaFile>> {
        return withContext(Dispatchers.IO) {
            try {
                val photoList = mutableListOf<SambaFile>()
                val root = if (path.isEmpty()) "" else path
                share?.let {
                    recursiveSearch(it, root, photoList)
                }
                Result.success(photoList)
            } catch (e: Exception) {
                Log.e("SambaRepository", "Error fetching photo list", e)
                Result.failure(e)
            }
        }
    }

    private fun recursiveSearch(share: DiskShare, path: String, photoList: MutableList<SambaFile>) {
        for (fileId in share.list(path)) {
            val fileName = fileId.fileName
            val newPath = if (path.isEmpty()) fileName else "$path/$fileName"
            if (fileId.fileAttributes and 16L > 0) { // DIRECTORY
                if (fileName != "." && fileName != "..") {
                    recursiveSearch(share, newPath, photoList)
                }
            } else {
                if (fileName.endsWith(".jpg", true) || fileName.endsWith(".png", true)) {
                    photoList.add(SambaFile(newPath, fileId.lastWriteTime.toEpochMillis()))
                }
            }
        }
    }

    override fun getShare(): DiskShare? {
        return share
    }
}
