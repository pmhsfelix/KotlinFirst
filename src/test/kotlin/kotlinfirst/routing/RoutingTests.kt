package kotlinfirst.routing

import java.util.*
import org.junit.Test as spec
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by pedro on 16/08/15.
 */

public class RoutingTests {

    spec fun it_works() {
        assertEquals(1, 1)
    }

    spec fun literal_string_is_not_a_variable() {
        assertNull("abc".tryGetVariableName());
    }

    spec fun name_in_curly_braces_is_a_variable() {
        assertEquals("name", "{name}".tryGetVariableName());
    }

    fun assertResult(target: Int, value: String, name:String, result : PathRouter.Result<Int>?){
        val (t, p) = result ?: PathRouter.Result(0, emptyMap<String,String>());
        assertEquals(target, t);
        assertEquals(value, p[name]);
    }

    spec fun router_can_register_const_paths() {
        val router = PathRouter<Int>();
        router.addPath("s1", 1);
        router.addPath("s1/s2", 2);
        router.addPath("s2", 3);
        router.addPath("{v1}", 4);
        router.addPath("s1/{v2}", 5);
        router.addPath("s1/{v2}/{v3}",6);
        assertEquals(1, router.find("s1")?.component1());
        assertEquals(2, router.find("s1/s2")?.component1());
        assertEquals(3, router.find("s2")?.component1());

        assertResult(4, "x1", "v1", router.find("x1"))

        assertResult(5, "x2", "v2", router.find("s1/x2"));
        assertResult(6, "x2", "v3", router.find("s1/x1/x2"));
    }
}