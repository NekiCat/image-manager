package rocks.tiger.imagemanager.tags

import rocks.tiger.imagemanager.EventSource

interface Tags : Set<Tag> {
	val onAdd: EventSource<Tag>
	val onRemove: EventSource<Tag>
}

interface MutableTags : Tags, MutableSet<Tag> {
	fun addAll(tags: String)
}
