package org.frice.game

import org.frice.game.Game.Initializer.launch
import org.frice.game.obj.sub.DebugImageObject
import org.frice.game.obj.sub.ImageObject
import org.frice.game.resource.image.ImageResource
import org.frice.game.utils.misc.BoolArray
import org.frice.game.utils.time.FTimeListener
import org.junit.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoolArrayTest {
	@Test
	fun test1() {
		val a = BoolArray(100)
		for (i in 0..99) assertFalse(a[i])
		a[0] = true
		assertTrue(a[0])
		a[1] = true
		assertTrue(a[1])
		a[10] = true
		assertTrue(a[10])
		a[10] = false
		assertFalse(a[10])
		a[50] = true
		assertTrue(a[50])
		a[50] = false
		assertFalse(a[50])
		a[99] = true
		assertTrue(a[99])
		(2..98)
				.forEach { assertFalse(a[it]) }
		assertFails { a[100] }
		assertFails { a[101] }
	}

	companion object {
		@JvmStatic
		fun main(vararg args: String) {
			launch(object : Game() {
				val dbg = DebugImageObject(
						ImageResource.fromWeb("https://coding.net/u/ice1000/p/Images/git/raw/master/blog-img/13/a.png"), 10.0, 10.0)

				override fun onLastInit() {
					addObject(dbg)
					dbg.debugMove(20.0, 20.0)
				}
			})
		}
	}
}
