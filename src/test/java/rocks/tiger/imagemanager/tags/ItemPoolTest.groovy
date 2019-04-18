package rocks.tiger.imagemanager.tags

import de.jodamob.kotlin.testrunner.OpenedClasses
import de.jodamob.kotlin.testrunner.SpotlinTestRunner
import org.junit.runner.RunWith
import spock.lang.Specification

@RunWith(SpotlinTestRunner)
@OpenedClasses(Tags)
class ItemPoolTest extends Specification {
	def "add item should subscribe"() {
		given:
		def pool = new ItemPool()
		def item = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> [].iterator()
			}
		}

		when:
		pool.add(item)

		then:
		1 * item.tags.subscribe(pool)
	}

	def "add should add item"() {
		given:
		def pool = new ItemPool()
		def item = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> [].iterator()
			}
		}

		expect:
		pool.add(item)
		!pool.add(item)

		and:
		pool.size() == 1
		pool.contains(item)
	}

	def "remove item should unsubscribe"() {
		given:
		def pool = new ItemPool()
		def item = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> [].iterator()
			}
		}

		and:
		pool.add(item)

		when:
		pool.remove(item)

		then:
		1 * item.tags.unsubscribe(pool)
	}

	def "remove should remove item"() {
		given:
		def pool = new ItemPool()
		def item = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> [].iterator()
			}
		}

		and:
		pool.add(item)

		expect:
		pool.remove(item)
		!pool.remove(item)

		and:
		pool.size() == 0
		!pool.contains(item)
	}

	def "clear should unsubscribe"() {
		given:
		def pool = new ItemPool()
		def item = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> [].iterator()
			}
		}

		and:
		pool.add(item)

		when:
		pool.clear()

		then:
		1 * item.tags.unsubscribe(pool)
	}

	def "clear should remove item"() {
		given:
		def pool = new ItemPool()
		def item = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> [].iterator()
			}
		}

		and:
		pool.add(item)

		when:
		pool.clear()

		then:
		pool.size() == 0
		!pool.contains(item)
	}

	def "filter should return correct items"() {
		given:
		def pool = new ItemPool()
		def item1 = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> ["1", "2"].iterator()
			}
		}
		def item2 = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> ["2", "3"].iterator()
			}
		}
		def item3 = Mock(TaggedItem) {
			getTags() >> Mock(Tags) {
				iterator() >> ["3", "4"].iterator()
			}
		}

		and:
		pool.add(item1)
		pool.add(item2)
		pool.add(item3)

		when:
		def result1 = pool.filter(new Tag("2"))

		then:
		result1.size() == 2
		result1.contains(item1)
		result1.contains(item2)

		when:
		def result2 = pool.filter(new Tag("1"), new Tag("2"))

		then:
		result2.size() == 1
		result2.contains(item1)

		when:
		def result3 = pool.filter(new Tag("nothing"))

		then:
		result3.isEmpty()
	}
}
