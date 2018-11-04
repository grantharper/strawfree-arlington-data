package org.grantharper.strawfree

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class StrawfreeApplicationTests {

    @Autowired
    lateinit var strawfreeApplication: StrawfreeApplication

    @Test
    fun contextLoads() {
        println("starCountHeader=${strawfreeApplication.starCountHeader}")

    }

    @Test
    fun callMapsApiTest() {
        val latlng = strawfreeApplication.callMapsAPI("501 E Monroe Ave, Alexandria, VA 22301")
        assertTrue(latlng.first != 0.0)
        assertTrue(latlng.second != 0.0)
        assertEquals(39, Math.round(latlng.first!!))
        assertEquals(-77, Math.round(latlng.second!!))

    }

}
