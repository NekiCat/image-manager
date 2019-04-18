package rocks.tiger.imagemanager.tags

import rocks.tiger.imagemanager.support.MockTaggedItem
import spock.lang.Specification

class ItemPoolTest extends Specification {
	def "add item should subscribe"() {
		given:
		def pool = new ItemPool()
		def item = new MockTaggedItem()

		when:
		pool.add(item)

		then:
		item.hasSubscribedWith(pool)
	}

	def "add should add item"() {
		given:
		def pool = new ItemPool()
		def item = new MockTaggedItem()

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
		def item = new MockTaggedItem()

		and:
		pool.add(item)

		when:
		pool.remove(item)

		then:
		item.hasUnSubscribedWith(pool)
	}

	def "remove should remove item"() {
		given:
		def pool = new ItemPool()
		def item = new MockTaggedItem()

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
		def item = new MockTaggedItem()

		and:
		pool.add(item)

		when:
		pool.clear()

		then:
		item.hasUnSubscribedWith(pool)
	}

	def "clear should remove item"() {
		given:
		def pool = new ItemPool()
		def item = new MockTaggedItem()

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
		def item1 = new MockTaggedItem("1 2")
		def item2 = new MockTaggedItem("2 3")
		def item3 = new MockTaggedItem("3 4")

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
