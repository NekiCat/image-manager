package rocks.tiger.imagemanager.filetypes

class Size(val width: Int, val height: Int) {
	constructor(size: Size) : this(size.width, size.height)
	constructor() : this(0, 0)

	override fun equals(other: Any?): Boolean {
		if (other !is Size) {
			return false
		}

		return width == other.width && height == other.height
	}

	override fun hashCode(): Int {
		val sum = width + height
		return sum * (sum + 1) / 2 + width
	}

	override fun toString(): String {
		return "Size[$width, $height]"
	}
}
