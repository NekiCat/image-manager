package rocks.tiger.imagemanager.tags

import spock.lang.Specification

class IndexTest extends Specification {
	def "add should add value once"() {
		given:
		def index = new Index()

		expect:
		index.add("1", "1")
		!index.add("1", "1")

		and:
		index.contains("1", "1")
		index.get("1").size() == 1
	}

	def "add with list should add all"() {
		given:
		def index = new Index()

		when:
		index.add(["1", "2"], "1")

		then:
		index.contains("1", "1")
		index.contains("2", "1")
	}

	def "remove should remove value"() {
		given:
		def index = new Index()
		index.add("1", "1")
		index.add("1", "2")
		index.add("2", "1")

		expect:
		index.remove("2", "1")

		and:
		index.contains("1", "1")
		index.contains("1", "2")
	}

	def "remove with list should remove all"() {
		given:
		def index = new Index()
		index.add("1", "1")
		index.add("1", "2")
		index.add("2", "1")

		when:
		index.remove(["1", "2"], "1")

		then:
		!index.contains("1", "1")
		!index.contains("2", "1")
		index.contains("1", "2")
	}

	def "remove should return false"() {
		given:
		def index = new Index()

		expect:
		!index.remove("1", "1")
	}

	def "clear should remove all"() {
		given:
		def index = new Index()
		index.add("1", "1")
		index.add("2", "1")

		when:
		index.clear()

		then:
		!index.contains("1", "1")
		!index.contains("2", "1")
	}
}
