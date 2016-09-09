package org.frice.game

import org.frice.game.event.OnClickEvent
import org.frice.game.event.OnKeyEvent
import org.frice.game.event.OnMouseEvent
import org.frice.game.event.OnWindowEvent
import org.frice.game.obj.sub.ImageObject
import org.frice.game.resource.FResource
import org.frice.game.resource.graphics.ColorResource
import org.frice.game.resource.image.ImageResource
import org.frice.game.utils.message.FDialog
import java.awt.BorderLayout
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.WindowConstants

/**
 * First game class(not for you)
 *
 * Standard library, mainly for GUI.
 * some other library is in @see
 *
 * Created by ice1000 on 2016/8/15.
 * @author ice1000
 * @since v0.2.3
 */
abstract class AbstractGame() : JFrame() {
	companion object {
		@JvmStatic val TO_X = 100
		@JvmStatic val TO_Y = 100

		@JvmStatic val SMALL_PHONE = Rectangle(TO_X, TO_Y, 480, 800)
		@JvmStatic val BIG_PHONE = Rectangle(TO_X, TO_Y, 720, 1200)
		@JvmStatic val HUGE_PHONE = Rectangle(TO_X, TO_Y, 1080, 1920)

		@JvmStatic val SMALL_SQUARE = Rectangle(TO_X, TO_Y, 400, 400)
		@JvmStatic val BIG_SQUARE = Rectangle(TO_X, TO_Y, 800, 800)

		@JvmStatic fun Rectangle.turn() {
			width -= -height
			height -= width
			width += height
		}
	}

	/**
	 * if paused, main window will not call `onRefresh()`.
	 */
	var paused = false

	/**
	 * not implemented yet.
	 * currently it's same as paused.
	 */
	var stopped = false

	/**
	 * background resource (don't setBackground, please use `setBack()` instead.)
	 */
	var back: FResource = ColorResource.LIGHT_GRAY
	var debug = true

	/**
	 * a general purpose instance for generating random numbers
	 */
	val random = Random()

	/**
	 * if true, the engine will collect all objects which are invisible from game window.
	 */
	var autoGC = true

	/**
	 * if true, there will be a fps calculating on the bottom-left side of window.
	 */
	var showFPS = true

	var loseFocus = false
		private set

	var loseFocusChangeColor = true

	init {
		isResizable = false
		defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
		layout = BorderLayout()
	}

	protected abstract fun mouse(e: OnMouseEvent)
	protected abstract fun click(e: OnClickEvent)

	protected open fun onInit() = Unit
	protected open fun onLastInit() = Unit
	protected open fun onRefresh() = Unit
	protected open fun onClick(e: OnClickEvent) = Unit
	protected open fun onMouse(e: OnMouseEvent) = Unit
	protected open fun onExit() {
		if (FDialog(this).confirm("Are you sure to exit?",
				"Ensuring", JOptionPane.YES_NO_OPTION) ==
				JOptionPane.YES_OPTION)
			System.exit(0)
		else return
	}

	protected open fun onLoseFocus(e: OnWindowEvent?) {
		paused = true
	}

	protected open fun onFocus(e: OnWindowEvent?) {
		paused = false
	}

	protected open fun customDraw(g: Graphics2D) = Unit

	/**
	 * for kotlin only
	 * add keyboard listeners with lambda
	 */
	fun addKeyListener(
			typed: (KeyEvent) -> Unit = { },
			pressed: (KeyEvent) -> Unit = { },
			released: (KeyEvent) -> Unit = { }) {
		addKeyListener(object : KeyListener {
			override fun keyPressed(e: KeyEvent?) = pressed(e!!)
			override fun keyReleased(e: KeyEvent?) = released(e!!)
			override fun keyTyped(e: KeyEvent?) = typed(e!!)
		})
	}

	fun listenKeyPressed(key: OnKeyEvent) = listenKeyPressed({ e -> key.execute(e) })
	fun listenKeyPressed(key: (KeyEvent) -> Unit) =
			addKeyListener({ key.invoke(it) }, { key.invoke(it) }, { key.invoke(it) })

	fun addKeyTypedEvent(keyCode: Int, key: OnKeyEvent) = addKeyTypedEvent(keyCode, { e -> key.execute(e) })
	fun addKeyTypedEvent(keyCode: Int, key: (KeyEvent) -> Unit) = addKeyListener(typed = { e ->
		if (e.keyCode == keyCode) key.invoke(e)
	})

	fun addKeyPressedEvent(keyCode: Int, key: OnKeyEvent) =
			addKeyPressedEvent(keyCode, { e -> key.execute(e) })

	fun addKeyPressedEvent(keyCode: Int, key: (KeyEvent) -> Unit) = addKeyListener(pressed = { e ->
		if (e.keyCode == keyCode) key.invoke(e)
	})

	fun addKeyReleasedEvent(keyCode: Int, key: OnKeyEvent) =
			addKeyReleasedEvent(keyCode, { e -> key.execute(e) })

	fun addKeyReleasedEvent(keyCode: Int, key: (KeyEvent) -> Unit) = addKeyListener(released = { e ->
		if (e.keyCode == keyCode) key.invoke(e)
	})

	fun setCursor(o: ImageResource) = setCursor(ImageObject(o))
	fun setCursor(o: ImageObject) {
		cursor = toolkit.createCustomCursor(o.getImage(), Point(0, 0), "cursor")
	}
}