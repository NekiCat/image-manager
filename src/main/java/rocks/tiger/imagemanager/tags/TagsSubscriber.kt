package rocks.tiger.imagemanager.tags

interface TagsSubscriber {
	fun onTagAdded(tag: Tag, sender: TaggedItem)
	fun onTagRemoved(tag: Tag, sender: TaggedItem)
}
