package rocks.tiger.imagemanager

interface EventSource<V> {
	fun subscribe(subscriber: (V) -> Unit): Int
	fun unsubscribe(subscriber: Int): Boolean

	fun <N> map(fn: (V) -> N): EventSource<N>
	fun filter(fn: (V) -> Boolean): EventSource<V>
}

class EventSourceContainer<V> {
	private var subscriberIndex = 0
	private val subscribers = HashMap<Int, (V) -> Unit>()

	operator fun invoke(value: V) {
		for (subscriber in subscribers.values) {
			subscriber(value)
		}
	}

	val source: EventSource<V> = object : EventSourceBase<V>() {
		override fun subscribe(subscriber: (V) -> Unit): Int {
			subscriberIndex += 1
			subscribers[subscriberIndex] = subscriber
			return subscriberIndex
		}

		override fun unsubscribe(subscriber: Int): Boolean {
			return subscribers.remove(subscriber) != null
		}
	}
}

private abstract class EventSourceBase<V> : EventSource<V> {
	override fun <N> map(fn: (V) -> N): EventSource<N> {
		return EventSourceMapping(this, fn)
	}

	override fun filter(fn: (V) -> Boolean): EventSource<V> {
		return EventSourceFiltering(this, fn)
	}
}

private class EventSourceMapping<V, N>(private val source: EventSource<V>, private val fn: (V) -> N) : EventSourceBase<N>() {
	override fun subscribe(subscriber: (N) -> Unit): Int {
		return source.subscribe { subscriber(fn(it)) }
	}

	override fun unsubscribe(subscriber: Int): Boolean {
		return source.unsubscribe(subscriber)
	}
}

private class EventSourceFiltering<V>(private val source: EventSource<V>, private val fn: (V) -> Boolean): EventSourceBase<V>() {
	override fun subscribe(subscriber: (V) -> Unit): Int {
		return source.subscribe { if (fn(it)) subscriber(it) }
	}

	override fun unsubscribe(subscriber: Int): Boolean {
		return source.unsubscribe(subscriber)
	}
}
