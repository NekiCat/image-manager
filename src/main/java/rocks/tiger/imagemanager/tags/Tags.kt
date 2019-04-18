package rocks.tiger.imagemanager.tags

class Tags(private val parent: TaggedItem) : HashSet<Tag>() {
	private val subscribers = mutableSetOf<TagsSubscriber>()

	override fun add(element: Tag): Boolean {
		val result = super.add(element)
		if (result) {
			onAdd(element)
		}

		return result
	}

	override fun remove(element: Tag): Boolean {
		val result = super.remove(element)
		if (result) {
			onRemove(element)
		}

		return result
	}

	override fun clear() {
		for (tag in this) {
			onRemove(tag)
		}

		super.clear()
	}

	private fun onAdd(tag: Tag) {
		subscribers.forEach { subscriber -> subscriber.onTagAdded(tag, parent) }
	}

	private fun onRemove(tag: Tag) {
		subscribers.forEach { subscriber -> subscriber.onTagRemoved(tag, parent) }
	}

	fun subscribe(subscriber: TagsSubscriber) {
		subscribers.add(subscriber)
	}

	fun unsubscribe(subscriber: TagsSubscriber) {
		subscribers.remove(subscriber)
	}
}
