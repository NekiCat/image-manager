package rocks.tiger.imagemanager.tags

import spock.lang.Specification

class TagsTest extends Specification {
	def "constructor should take tags"() {
		given:
		def item = Mock(TaggedItem)

		when:
		def tags = new Tags(item, "these are tags")

		then:
		tags.size() == 3
		tags.any { it.tag == "these" }
		tags.any { it.tag == "are" }
		tags.any { it.tag == "tags" }
	}

	def "toString should make tags"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)

		and:
		tags.add(new Tag("tag1"))
		tags.add(new Tag("tag2"))
		tags.add(new Tag("tag3"))

		expect:
		tags.toString() == "tag1 tag2 tag3"
	}

	def "should save tags"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def tag = new Tag("tag")

		when:
		tags.add(tag)

		then:
		tags.size() == 1
		tags[0] == tag
	}

	def "should save tags only once"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def tag = new Tag("tag")

		when:
		tags.addAll([tag, tag])

		then:
		tags.size() == 1
		tags[0] == tag
	}

	def "should add subscribers"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def tag = new Tag("tag")
		def subscriber1 = Mock(TagsSubscriber)
		def subscriber2 = Mock(TagsSubscriber)

		when:
		tags.subscribe(subscriber1)
		tags.subscribe(subscriber2)

		and:
		tags.add(tag)

		then:
		1 * subscriber1.onTagAdded(tag, item)
		1 * subscriber2.onTagAdded(tag, item)
	}

	def "should remove subscribers"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def tag = new Tag("tag")
		def subscriber1 = Mock(TagsSubscriber)
		def subscriber2 = Mock(TagsSubscriber)

		when:
		tags.subscribe(subscriber1)
		tags.subscribe(subscriber2)

		and:
		tags.unsubscribe(subscriber1)

		and:
		tags.add(tag)

		then:
		//noinspection GroovyAssignabilityCheck
		0 * subscriber1.onTagAdded(*_)
		1 * subscriber2.onTagAdded(tag, item)
	}

	def "should notify addAll"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def tag = new Tag("tag")
		def subscriber = Mock(TagsSubscriber)
		tags.subscribe(subscriber)

		when:
		tags.addAll([tag, tag])

		then:
		1 * subscriber.onTagAdded(tag, item)
	}

	def "should notify remove"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def tag = new Tag("tag")
		def subscriber = Mock(TagsSubscriber)
		tags.subscribe(subscriber)
		tags.add(tag)

		when:
		tags.remove(tag)

		then:
		tags.isEmpty()

		and:
		1 * subscriber.onTagRemoved(tag, item)
	}

	def "should notify remove on clear"() {
		given:
		def item = Mock(TaggedItem)
		def tags = new Tags(item)
		def subscriber = Mock(TagsSubscriber)
		tags.subscribe(subscriber)
		tags.addAll([new Tag("1"), new Tag("2"), new Tag("3")])

		when:
		tags.clear()

		then:
		//noinspection GroovyAssignabilityCheck
		3 * subscriber.onTagRemoved(_, item)
	}
}
