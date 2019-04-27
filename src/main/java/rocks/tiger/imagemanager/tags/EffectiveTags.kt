package rocks.tiger.imagemanager.tags

import rocks.tiger.imagemanager.EventSourceContainer

class EffectiveTags(private val tags: Tags, private val implications: Index<Tag, Tag>) : Tags {
	private val all = HashSet<Tag>()
	private val addEvent = EventSourceContainer<Tag>()
	private val removeEvent = EventSourceContainer<Tag>()

	override val onAdd = addEvent.source
	override val onRemove = removeEvent.source

	init {
		all.addAll(tags)
		for (tag in tags) {
			all.addAll(implications[tag])
		}

	    tags.onAdd.subscribe { tag ->
			val new = implications[tag].toMutableSet()
			new.add(tag)
			new.removeAll(all)

			all.addAll(new)

			new.forEach { addEvent(it) }
		}

		tags.onRemove.subscribe { tag ->
			val old = implications[tag].toMutableSet()
			old.removeAll(tags)
			old.add(tag)

			all.removeAll(old)

			old.forEach { removeEvent(it) }
		}
	}

	override fun iterator(): Iterator<Tag> = all.iterator()

	override val size get() = all.size

	override fun contains(element: Tag): Boolean = all.contains(element)

	override fun containsAll(elements: Collection<Tag>): Boolean = all.containsAll(elements)

	override fun isEmpty(): Boolean = all.isEmpty()
}
