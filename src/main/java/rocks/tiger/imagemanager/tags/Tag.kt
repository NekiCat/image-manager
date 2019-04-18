package rocks.tiger.imagemanager.tags

class Tag(tag: String) {
	val tag = tag.trim().replace("\\s+".toRegex(), "_")

	override fun equals(other: Any?): Boolean {
		if (other is String) {
			return tag == other
		}

		if (other !is Tag) {
			return false
		}

		return tag == other.tag
	}

	override fun hashCode() = tag.hashCode()

	override fun toString() = tag
}
