package rocks.tiger.imagemanager.support

import org.jetbrains.annotations.NotNull
import rocks.tiger.imagemanager.tags.TaggedItem
import rocks.tiger.imagemanager.tags.Tags
import rocks.tiger.imagemanager.tags.TagsSubscriber

class MockTaggedItem implements TaggedItem {
	private Tags tags
	private TagsSubscriber subscriber
	private TagsSubscriber unSubscriber

	public MockTaggedItem(String tags) {
		if (tags == null) {
			this.tags = new Tags(this)
		} else {
			this.tags = new Tags(this, tags)
		}
	}

	Tags getTags() {
		return tags
	}

	void subscribe(@NotNull TagsSubscriber subscriber) {
		this.subscriber = subscriber
	}

	void unsubscribe(@NotNull TagsSubscriber subscriber) {
		unSubscriber = subscriber
	}

	boolean hasSubscribedWith(@NotNull TagsSubscriber subscriber) {
		return this.subscriber == subscriber
	}

	boolean hasUnSubscribedWith(@NotNull TagsSubscriber subscriber) {
		return unSubscriber == subscriber
	}
}
