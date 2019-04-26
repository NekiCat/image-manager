package rocks.tiger.imagemanager.tags

data class TagChangeEvent(val tag: Tag, val item: TaggedItem)

abstract class TaggedItem {
	val tags = BaseTags()

	open val onAdd = tags.onAdd.map { tag -> TagChangeEvent(tag, this) }
	open val onRemove = tags.onRemove.map { tag -> TagChangeEvent(tag, this) }
}
