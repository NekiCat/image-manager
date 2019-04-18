package rocks.tiger.imagemanager.tags

class ItemPool : TagsSubscriber, HashSet<TaggedItem>() {
	private val tagIndex = Index<Tag, TaggedItem>()

	override fun add(element: TaggedItem): Boolean {
		element.subscribe(this)
		tagIndex.add(element.tags, element)

		return super.add(element)
	}

	override fun clear() {
		this.forEach { it.unsubscribe(this) }
		tagIndex.clear()
		super.clear()
	}

	override fun remove(element: TaggedItem): Boolean {
		element.unsubscribe(this)
		tagIndex.remove(element.tags, element)

		return super.remove(element)
	}

	override fun onTagAdded(tag: Tag, sender: TaggedItem) {
		tagIndex.add(tag, sender)
	}

	override fun onTagRemoved(tag: Tag, sender: TaggedItem) {
		tagIndex.remove(tag, sender)
	}

	fun filter(vararg tags: Tag): Set<TaggedItem> {
		var result: MutableSet<TaggedItem> = this
		for (tag in tags) {
			result = result.intersect(tagIndex[tag]) as MutableSet<TaggedItem>
		}

		return result
	}
}
