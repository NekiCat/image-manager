package rocks.tiger.imagemanager.tags

import kotlin.jvm.functions.Function1
import spock.lang.Specification

class BaseTagsTest extends Specification {
	def "constructor should take tags"() {
		when:
		def tags = new BaseTags("these are tags")

		then:
		tags.size() == 3
		tags.any { it.tag == "these" }
		tags.any { it.tag == "are" }
		tags.any { it.tag == "tags" }
	}

	def "toString should make tags"() {
		given:
		def tags = new BaseTags()

		and:
		tags.add(new Tag("tag1"))
		tags.add(new Tag("tag2"))
		tags.add(new Tag("tag3"))

		expect:
		tags.toString() == "tag1 tag2 tag3"
	}

	def "should save tags"() {
		given:
		def tags = new BaseTags()
		def tag = new Tag("tag")

		when:
		tags.add(tag)

		then:
		tags.size() == 1
		tags[0] == tag
	}

	def "should save tags only once"() {
		given:
		def tags = new BaseTags()
		def tag = new Tag("tag")

		when:
		tags.addAll([tag, tag])

		then:
		tags.size() == 1
		tags[0] == tag
	}

	def "should notify addAll"() {
		given:
		def tags = new BaseTags()
		def tag = new Tag("tag")
		def subscriber = Mock(Function1)
		tags.onAdd.subscribe(subscriber)

		when:
		tags.addAll([tag, tag])

		then:
		1 * subscriber.invoke(tag)
	}

	def "should notify remove"() {
		given:
		def tags = new BaseTags()
		def tag = new Tag("tag")
		def subscriber = Mock(Function1)
		tags.onRemove.subscribe(subscriber)
		tags.add(tag)

		when:
		tags.remove(tag)

		then:
		tags.isEmpty()

		and:
		1 * subscriber.invoke(tag)
	}

	def "should notify remove on clear"() {
		given:
		def tags = new BaseTags()
		def subscriber = Mock(Function1)
		tags.onRemove.subscribe(subscriber)
		tags.addAll([new Tag("1"), new Tag("2"), new Tag("3")])

		when:
		tags.clear()

		then:
		3 * subscriber.invoke(_)
	}
}
