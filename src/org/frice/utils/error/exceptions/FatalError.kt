package org.frice.utils.error.exceptions

/**
 * Created by ice1000 on 2016/8/14.
 * @author ice1000
 * @since v0.2
 */
class FatalError(s: String) : Error(s) {
	constructor(): this("")
}