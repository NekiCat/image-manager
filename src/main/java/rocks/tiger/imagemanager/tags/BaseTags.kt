package rocks.tiger.imagemanager.tags

import rocks.tiger.imagemanager.EventSourceContainer

class BaseTags() : MutableTags, HashSet<Tag>() {
	private val addEvent = EventSourceContainer<Tag>()
	private val removeEvent = EventSourceContainer<Tag>()

	override val onAdd = addEvent.source
	override val onRemove = removeEvent.source

	constructor(tags: String) : this() {
		addAll(tags)
	}

	override fun add(element: Tag): Boolean {
		val result = super.add(element)
		if (result) {
			addEvent(element)
		}

		return result
	}

	override fun addAll(tags: String) {
		tags.trim().split("\\s+".toRegex()).map { Tag(it) }.forEach { add(it) }
	}

	override fun remove(element: Tag): Boolean {
		val result = super.remove(element)
		if (result) {
			removeEvent(element)
		}

		return result
	}

	override fun clear() {
		for (tag in this) {
			removeEvent(tag)
		}

		super.clear()
	}

	override fun toString() = joinToString(" ")
}
