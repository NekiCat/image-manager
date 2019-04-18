package rocks.tiger.imagemanager.tags

import spock.lang.Specification

class TagTest extends Specification {
	def "tag should be set"() {
		given:
		def tag = new Tag("tag")

		expect:
		tag.tag == "tag"
	}

	def "tag should be processed"() {
		given:
		def tag = new Tag("a  new tag ")

		expect:
		tag.tag == "a_new_tag"
	}

	def "tag should be equal"() {
		given:
		def tag1 = new Tag("tag")
		def tag2 = new Tag("tag")

		expect:
		tag1 == tag2
		tag1.hashCode() == tag2.hashCode()
	}

	def "tag should compare to other objects"() {
		given:
		def tag = new Tag("tag")

		expect:
		//noinspection GrEqualsBetweenInconvertibleTypes
		tag == "tag"
		tag != new Object()
	}

	def "tag should not be equal"() {
		given:
		def tag1 = new Tag("tag1")
		def tag2 = new Tag("tag2")

		expect:
		tag1 != tag2
		tag1.hashCode() != tag2.hashCode()
	}
}
