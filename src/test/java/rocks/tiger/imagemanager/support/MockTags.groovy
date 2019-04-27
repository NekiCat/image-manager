package rocks.tiger.imagemanager.support

import org.jetbrains.annotations.NotNull
import rocks.tiger.imagemanager.EventSource
import rocks.tiger.imagemanager.tags.MutableTags
import rocks.tiger.imagemanager.tags.Tag

class MockTags extends HashSet<Tag> implements MutableTags {
	private EventSource<Tag> onAdd
	private EventSource<Tag> onRemove

	MockTags(EventSource<Tag> onAdd, EventSource<Tag> onRemove, Collection<Tag> collection) {
		super(collection)

		this.onAdd = onAdd
		this.onRemove = onRemove
	}

	MockTags(EventSource<Tag> onAdd, EventSource<Tag> onRemove, String tags) {
		super()

		addAll(tags)

		this.onAdd = onAdd
		this.onRemove = onRemove
	}

	MockTags(EventSource<Tag> onAdd, EventSource<Tag> onRemove) {
		super()

		this.onAdd = onAdd
		this.onRemove = onRemove
	}

	EventSource<Tag> getOnAdd() {
		return onAdd
	}

	EventSource<Tag> getOnRemove() {
		return onRemove
	}

	void addAll(@NotNull String tags) {
		for (def tag : tags.trim().split("\\s+")) {
			super.add(new Tag(tag))
		}
	}

	boolean remove(Tag o) {
		return super.remove(o)
	}

	int getSize() {
		return super.size()
	}

	boolean contains(Tag o) {
		return super.contains(o)
	}
}
