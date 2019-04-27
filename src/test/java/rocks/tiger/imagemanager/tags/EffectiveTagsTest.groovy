package rocks.tiger.imagemanager.tags

import kotlin.jvm.functions.Function1
import rocks.tiger.imagemanager.EventSource
import rocks.tiger.imagemanager.EventSourceContainer
import rocks.tiger.imagemanager.support.MockTags
import spock.lang.Specification

class EffectiveTagsTest extends Specification {
	def "it should access base tags"() {
		given:
		def tags = new MockTags(Mock(EventSource), Mock(EventSource), "tag")

		when:
		def effective = new EffectiveTags(tags, new Index())

		then:
		effective.contains(new Tag("tag"))

		and:
		1 * tags.onAdd.subscribe(_)
		1 * tags.onRemove.subscribe(_)
		0 * _
	}

	def "it should add tag"() {
		given:
		def add = new EventSourceContainer()
		def tags = new MockTags(add.source, Mock(EventSource))
		def effective = new EffectiveTags(tags, new Index<Tag, Tag>())

		and:
		def addWatch = Mock(Function1)
		effective.onAdd.subscribe(addWatch)

		when:
		add.invoke(new Tag("tag"))

		then:
		effective.contains(new Tag("tag"))

		and:
		1 * addWatch.invoke(new Tag("tag"))
	}

	def "it should remove tag"() {
		given:
		def remove = new EventSourceContainer()
		def tags = new MockTags(Mock(EventSource), remove.source, "tag")
		def effective = new EffectiveTags(tags, new Index<Tag, Tag>())

		and:
		def removeWatch = Mock(Function1)
		effective.onRemove.subscribe(removeWatch)

		when:
		remove.invoke(new Tag("tag"))

		then:
		!effective.contains(new Tag("tag"))

		and:
		1 * removeWatch.invoke(new Tag("tag"))
	}

	def "it should have implied"() {
		given:
		def tags = new MockTags(Mock(EventSource), Mock(EventSource), "tag")
		def index = new Index<Tag, Tag>()
		index.add(new Tag("tag"), new Tag("implied"))

		when:
		def effective = new EffectiveTags(tags, index)

		then:
		effective.contains(new Tag("tag"))
		effective.contains(new Tag("implied"))
	}

	def "it should add implied"() {
		given:
		def add = new EventSourceContainer()
		def tags = new MockTags(add.source, Mock(EventSource))
		def index = new Index<Tag, Tag>()
		index.add(new Tag("tag"), new Tag("implied"))
		def effective = new EffectiveTags(tags, index)

		and:
		def addWatch = Mock(Function1)
		effective.onAdd.subscribe(addWatch)

		expect:
		!effective.contains(new Tag("implied"))

		when:
		add.invoke(new Tag("tag"))

		then:
		effective.contains(new Tag("tag"))
		effective.contains(new Tag("implied"))

		and:
		1 * addWatch.invoke(new Tag("tag"))
		1 * addWatch.invoke(new Tag("implied"))
	}

	def "it should remove implied"() {
		given:
		def remove = new EventSourceContainer()
		def tags = new MockTags(Mock(EventSource), remove.source, "tag")
		def index = new Index<Tag, Tag>()
		index.add(new Tag("tag"), new Tag("implied"))
		def effective = new EffectiveTags(tags, index)

		and:
		def removeWatch = Mock(Function1)
		effective.onRemove.subscribe(removeWatch)

		when:
		remove.invoke(new Tag("tag"))

		then:
		!effective.contains(new Tag("tag"))
		!effective.contains(new Tag("implied"))

		and:
		1 * removeWatch.invoke(new Tag("tag"))
		1 * removeWatch.invoke(new Tag("implied"))
	}

	def "it should not remove explicit"() {
		given:
		def remove = new EventSourceContainer()
		def tags = new MockTags(Mock(EventSource), remove.source, "tag explicit")
		def index = new Index<Tag, Tag>()
		index.add(new Tag("tag"), new Tag("implicit"))
		index.add(new Tag("tag"), new Tag("explicit"))
		def effective = new EffectiveTags(tags, index)

		and:
		def removeWatch = Mock(Function1)
		effective.onRemove.subscribe(removeWatch)

		when:
		remove.invoke(new Tag("tag"))

		then:
		effective.contains(new Tag("explicit"))
		!effective.contains(new Tag("implicit"))

		and:
		0 * removeWatch.invoke(new Tag("explicit"))
		1 * removeWatch.invoke(new Tag("implicit"))
	}
}
