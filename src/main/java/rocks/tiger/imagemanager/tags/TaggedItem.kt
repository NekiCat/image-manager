package rocks.tiger.imagemanager.tags

interface TaggedItem {
	val tags: Tags

	fun subscribe(subscriber: TagsSubscriber) = tags.subscribe(subscriber)
	fun unsubscribe(subscriber: TagsSubscriber) = tags.unsubscribe(subscriber)
}
