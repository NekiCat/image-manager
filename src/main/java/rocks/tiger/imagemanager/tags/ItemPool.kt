package rocks.tiger.imagemanager.tags

class ItemPool : HashSet<TaggedItem>() {
	private val addSubscriptionIndex = HashMap<TaggedItem, Int>()
	private val removeSubscriptionIndex = HashMap<TaggedItem, Int>()
	private val tagIndex = Index<Tag, TaggedItem>()

	override fun add(element: TaggedItem): Boolean {
		addSubscriptionIndex[element] = element.onAdd.subscribe { e -> tagIndex.add(e.tag, e.item) }
		removeSubscriptionIndex[element] = element.onRemove.subscribe { e -> tagIndex.remove(e.tag, e.item) }

		tagIndex.add(element.tags, element)

		return super.add(element)
	}

	override fun clear() {
		this.forEach {
			it.onAdd.unsubscribe(addSubscriptionIndex[it]!!)
			it.onRemove.unsubscribe(removeSubscriptionIndex[it]!!)
		}

		addSubscriptionIndex.clear()
		removeSubscriptionIndex.clear()
		tagIndex.clear()
		super.clear()
	}

	override fun remove(element: TaggedItem): Boolean {
		element.onAdd.unsubscribe(addSubscriptionIndex[element]!!)
		element.onRemove.unsubscribe(removeSubscriptionIndex[element]!!)

		tagIndex.remove(element.tags, element)

		return super.remove(element)
	}

	fun filter(vararg tags: Tag): Set<TaggedItem> {
		var result: MutableSet<TaggedItem> = this
		for (tag in tags) {
			result = result.intersect(tagIndex[tag]) as MutableSet<TaggedItem>
		}

		return result
	}
}
