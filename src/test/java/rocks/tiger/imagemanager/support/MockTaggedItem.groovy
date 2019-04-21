package rocks.tiger.imagemanager.support

import rocks.tiger.imagemanager.EventSource
import rocks.tiger.imagemanager.tags.TagChangeEvent
import rocks.tiger.imagemanager.tags.TaggedItem

class MockTaggedItem extends TaggedItem {
	private EventSource<TagChangeEvent> add
	private EventSource<TagChangeEvent> remove

	MockTaggedItem(EventSource<TagChangeEvent> add, EventSource<TagChangeEvent> remove, String tags = null) {
		this.add = add
		this.remove = remove

		if (tags != null) {
			this.tags.addAll(tags)
		}
	}

	@Override
	EventSource<TagChangeEvent> getOnAdd() {
		return add
	}

	@Override
	EventSource<TagChangeEvent> getOnRemove() {
		return remove
	}
}
