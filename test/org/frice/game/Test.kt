package org.frice.game

import org.frice.game.anim.RotateAnim
import org.frice.game.anim.move.*
import org.frice.game.anim.scale.SimpleScale
import org.frice.game.event.OnClickEvent
import org.frice.game.obj.PhysicalObject
import org.frice.game.obj.button.SimpleButton
import org.frice.game.obj.button.SimpleText
import org.frice.game.obj.effects.FunctionEffect
import org.frice.game.obj.effects.ParticleEffect
import org.frice.game.obj.sub.ImageObject
import org.frice.game.obj.sub.ShapeObject
import org.frice.game.platform.adapter.JvmImage
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.resource.graphics.ParticleResource
import org.frice.game.resource.image.WebImageResource
import org.frice.game.utils.data.Preference
import org.frice.game.utils.data.XMLPreference
import org.frice.game.utils.graphics.shape.*
import org.frice.game.utils.graphics.utils.grayify
import org.frice.game.utils.message.FLog
import org.frice.game.utils.time.FTimeListener
import org.frice.game.utils.time.FTimer
import java.util.*
import kotlin.test.assertEquals

/**
 * Sample
 * Created by ice1000 on 2016/8/21.
 *
 * @author ice1000
 */
class Test : Game() {
	private lateinit var preference: Preference
	private lateinit var xmlPreference: XMLPreference
	private val timer = FTimer(200)
	private val objs = LinkedList<PhysicalObject>()

	override fun onInit() {
		super.onInit()
		autoGC = true

		addTimeListener(FTimeListener(400, timeUp = { FLog.v("400 ms has passed") }))

		addObject(ParticleEffect(ParticleResource(
				this, width / 10, height / 10, 0.01), width * 0.1, height * 0.1))
		addObject(SimpleButton(
				text = "I am a button",
				x = 30.0,
				y = 30.0,
				width = 100.0,
				height = 30.0).apply {
			onClickListener = {
				val obj = ShapeObject(ColorResource.Companion.西木野真姬, FOval(40.0, 30.0), 100.0, 100.0).apply {
					mass = 1.0
					addForce(-1.0, -1.0)
					anims.add(SimpleMove(400, 400))
					anims.add(SimpleScale(1.1, 1.1))
					anims.add(RotateAnim(0.1))
				}
				objs.add(obj)
				addObject(obj)
			}
		})
//		getPlayer("1.wav").start()
//		play("1.wav")

//		setCursor(WebImageResource("https://avatars1.githubusercontent.com/u/16477304?v=3&s=84"))

		preference = Preference("settings.properties")
		preference.insert("fuck", "microsoft")

		xmlPreference = XMLPreference("settings.xml")
		xmlPreference.insert("shit", "goddamn it")

		FLog.v(preference.query("fuck", "Apple"))
		FLog.v(xmlPreference.query("shit", "no we don't"))

//		FOval(1.0, 1.0)
		FCircle(1.0)
		FPoint(1, 2)

//		addObject(ImageObject(FileImageResource("1.png"), 10.0, 10.0))

		FLog.v(ColorResource.小泉花阳.color.rgb.grayify())
	}

	override fun onRefresh() {
		super.onRefresh()
		if (timer.ended()) {
			objs.removeAll { o -> o.died }
			addObject(ShapeObject(ColorResource.Companion.IntelliJ_IDEA黑, FCircle(10.0),
					mouse.x, mouse.y).apply {
				anims.add(AccelerateMove.getGravity())
				anims.add(AccurateMove(random.nextInt(400) - 200.0, 0.0))
				targets.clear()
				objs.forEach { o ->
					addCollider(o, {
						anims.clear()
						targets.clear()
						anims.add(SimpleMove(0, -300))
						anims.add(SimpleScale(1.1, 1.1))
						res = ColorResource.MAGENTA
					})
				}
			})
		}
	}

	override fun onClick(e: OnClickEvent) {
		super.onClick(e)
		FLog.v(e.toString())
		FLog.v(mouse)
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(Test::class.java)
		}
	}
}

class TestImage : Game() {
	override fun onInit() {
		super.onInit()
		addObject(ImageObject((WebImageResource(
				"https://avatars1.githubusercontent.com/u/21008243?v=3&s=200").image as JvmImage).greenify(),
				10.0, 10.0))
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) = launch(TestImage::class.java)
	}
}


/**
 * Created by ice1000 on 2016/9/11.
 *
 * @author ice1000
 */
class Test2 : Game() {
	val timer = FTimer(200)
	lateinit var obj: ShapeObject
	lateinit var obj2: ShapeObject

	@org.junit.Test
	override fun onInit() {
		obj2 = ShapeObject(ColorResource.Companion.天依蓝, FRectangle(20, 20), 200.0, 200.0, 233).apply {
			mass = 2.0
		}
		obj = ShapeObject(ColorResource.Companion.西木野真姬, FCircle(30.0), 100.0, 100.0, 233).apply {
			mass = 1.0
			anims.add(SimpleMove(80, 0))
		}
		val text = SimpleText(ColorResource.BLUE, "this is a text demo", 100.0, 300.0)
		text.textSize = 64
		assertEquals(obj, obj2)
		addObject(obj2, obj, text)
	}

	override fun onRefresh() {
		if (timer.ended()) {
		}
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) = launch(Test2::class.java)
	}
}


/**
 * Created by ice1000 on 2016/10/22.
 *
 * @author ice1000
 */
class Test3 : Game() {
	lateinit var a: ShapeObject
	val d = 3.14 * 6
	val e = 0.1
	val c = 0.1
	override fun onInit() {
		setSize(1000, 1000)
		a = ShapeObject(ColorResource.BLUE, FCircle(10.0), 100.0, 500.0)
		a.anims.add(object : CustomMove() {
			override fun getXDelta(timeFromBegin: Double) =
					(a.x * c * Math.sin(d) - a.y * c * Math.cos(d)) * e

			override fun getYDelta(timeFromBegin: Double) =
					(a.x * c * Math.cos(d) - a.y * c * Math.sin(d)) * e
		})
		addObject(a)
	}

	override fun onExit() = System.exit(0)

	companion object {
		@JvmStatic
		fun main(args: Array<String>) = launch(Test3::class.java)
	}
}

class Test4 : Game(2) {
	override fun onInit() {
		addObject(0, FunctionEffect({
			Math.sin(it / 10) * 10 + 100
		}, 10.0, 10.0, 300, 300))
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) = launch(Test4::class.java)
	}
}

