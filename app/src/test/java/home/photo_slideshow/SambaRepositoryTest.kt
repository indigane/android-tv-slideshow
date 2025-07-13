package home.photo_slideshow

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SambaRepositoryTest {

    private lateinit var sambaRepository: SambaRepository

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        sambaRepository = SambaRepository.getInstance()
    }

    @Test(timeout = 60000)
    fun testConnect() {
        runBlocking {
            val result = sambaRepository.connect("127.0.0.1", "TEST", "guest", "", 4445)
            println("Connect result: $result")
            assert(result.isSuccess)
        }
    }
}
