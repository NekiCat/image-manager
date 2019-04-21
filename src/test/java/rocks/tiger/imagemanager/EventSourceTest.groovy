package rocks.tiger.imagemanager

import kotlin.jvm.functions.Function1
import spock.lang.Specification

class EventSourceTest extends Specification {
	def "should subscribe"() {
		given:
		def container = new EventSourceContainer()
		def subscriber = Mock(Function1)
		container.source.subscribe(subscriber)

		when:
		container.invoke(13)

		then:
		1 * subscriber.invoke(13)
	}

	def "should unsubscribe"() {
		given:
		def container = new EventSourceContainer()
		def subscriber = Mock(Function1)
		def token = container.source.subscribe(subscriber)

		and:
		container.source.unsubscribe(token)

		when:
		container.invoke(13)

		then:
		0 * subscriber.invoke(_)
	}

	def "should unsubscribe only one"() {
		given:
		def container = new EventSourceContainer()
		def sub1 = Mock(Function1)
		def sub2 = Mock(Function1)
		def token1 = container.source.subscribe(sub1)
		def token2 = container.source.subscribe(sub2)
		container.source.unsubscribe(token1)

		when:
		container.invoke(13)

		then:
		token1 != token2

		and:
		0 * sub1.invoke(_)
		1 * sub2.invoke(13)
	}

	def "should map value"() {
		given:
		def container = new EventSourceContainer()
		def subscriber = Mock(Function1)
		def mapper = Mock(Function1) {
			1 * invoke(13) >> 5
		}

		and:
		container.source.map(mapper).subscribe(subscriber)

		when:
		container.invoke(13)

		then:
		1 * subscriber.invoke(5)
	}

	def "should filter value"() {
		given:
		def container = new EventSourceContainer()
		def subscriber = Mock(Function1)
		def filterer = Mock(Function1) {
			1 * invoke(5) >> true
			1 * invoke(13) >> false
		}

		and:
		container.source.filter(filterer).subscribe(subscriber)

		when:
		container.invoke(5)
		container.invoke(13)

		then:
		1 * subscriber.invoke(5)
		0 * subscriber.invoke(_)
	}
}
