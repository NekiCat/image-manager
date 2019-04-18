package rocks.tiger.imagemanager.tags

class Index<K, V> {
	private val map = HashMap<K, MutableSet<V>>()

	fun add(key: K, value: V): Boolean {
		return map.computeIfAbsent(key) { mutableSetOf() }.add(value)
	}

	fun add(keys: Iterable<K>, value: V) {
		keys.forEach { add(it, value) }
	}

	fun remove(key: K, value: V): Boolean {
		return map[key]?.let { set ->
			val result = set.remove(value)
			if (set.isEmpty()) {
				map.remove(key)
			}

			return result
		} ?: false
	}

	fun remove(keys: Iterable<K>, value: V) {
		keys.forEach { remove(it, value) }
	}

	fun clear() {
		map.clear()
	}

	fun contains(key: K, value: V): Boolean {
		return map[key]?.contains(value) ?: false
	}

	operator fun get(key: K): Set<V> {
		return map[key] ?: setOf()
	}
}
