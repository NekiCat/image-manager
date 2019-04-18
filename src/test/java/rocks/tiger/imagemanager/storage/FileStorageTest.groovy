package rocks.tiger.imagemanager.storage

import rocks.tiger.imagemanager.tags.ItemPool
import spock.lang.Specification

import java.nio.file.Paths

class FileStorageTest extends Specification {
	def "should load from stream"() {
		given:
		def storage = new FileStorage()
		def stream = getClass().getResourceAsStream("/images.xml")

		when:
		def pool = storage.loadFromStream(stream)

		then:
		pool.size() == 1
		def file = pool[0] as TaggedFile
		file.id == UUID.fromString("16b8c961-0b76-4fd9-88b0-3bcc19b7548d")
		file.hash.type == "sha1"
		file.hash.value == "2346ad27d7568ba9896f1b7da6b5991251debdf2"
		file.path.toString() == "C:\\image.jpg"
		file.tags.toString() == "tag1 tag2 tag3"
	}

	def "should save to stream"() {
		given:
		def storage = new FileStorage()
		def comparison = new String(getClass().getResourceAsStream("/images.xml").bytes, "utf-8")
			.replaceAll(">\\s+<", "><").trim()
		def stream = new ByteArrayOutputStream()
		def pool = new ItemPool()
		def file = new TaggedFile(
			Paths.get("C:\\image.jpg"),
			UUID.fromString("16b8c961-0b76-4fd9-88b0-3bcc19b7548d"),
			new Hash("sha1", "2346ad27d7568ba9896f1b7da6b5991251debdf2")
		)

		and:
		file.tags.addAll("tag1 tag2 tag3")
		pool.add(file)

		when:
		storage.saveToStream(stream, pool)

		then:
		new String(stream.toByteArray(), "utf-8") == comparison
	}
}
