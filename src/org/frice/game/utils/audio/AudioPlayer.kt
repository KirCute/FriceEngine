package org.frice.game.utils.audio

import org.frice.game.utils.misc.forceRun
import org.frice.game.utils.misc.until
import java.io.File
import javax.sound.sampled.*

/**
 * From https://github.com/ice1000/Dekoder
 *
 * Created by ice1000 on 2016/8/16.
 * @author ice1000
 * @since v0.3.1
 */
class AudioPlayer internal constructor(file: File) {
	internal constructor(path: String) : this(File(path))

	internal fun main() {
		forceRun { line.open() }
		line.start()
		var inBytes = 0
		val audioData = ByteArray(BUFFER_SIZE)
		until(inBytes == -1 || exited) {
			inBytes = audioInputStream.read(audioData, 0, BUFFER_SIZE)
			if (inBytes >= 0) line.write(audioData, 0, inBytes)
		}
		line.drain()
		line.close()
	}

	private val thread = Thread(this::main)

	companion object LineGetter {
		@JvmField val BUFFER_SIZE = 2048

		fun getLine(audioFormat: AudioFormat): SourceDataLine {
			val info = DataLine.Info(SourceDataLine::class.java, audioFormat)
			val res = AudioSystem.getLine(info) as SourceDataLine
			res.open(audioFormat)
			return res
		}
	}

	private var audioInputStream: AudioInputStream
	private var format: AudioFormat
	private var line: SourceDataLine

	init {
		audioInputStream = AudioSystem.getAudioInputStream(file)
		format = audioInputStream.format
		if (format.encoding != AudioFormat.Encoding.PCM_SIGNED) {
			format = AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					format.sampleRate,
					16,
					format.channels,
					format.channels * 2,
					format.sampleRate,
					false)
			audioInputStream = AudioSystem.getAudioInputStream(format, audioInputStream)
		}
		line = getLine(format)

	}

	private var exited = false

	fun exit() {
		exited = true
		thread.join()
	}

	fun start() = thread.start()
}
