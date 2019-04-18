package rocks.tiger.imagemanager.storage.handler

import org.w3c.dom.Node
import rocks.tiger.imagemanager.getElementsByTagName
import rocks.tiger.imagemanager.storage.Hash
import rocks.tiger.imagemanager.storage.TaggedFile
import rocks.tiger.imagemanager.toUUID
import java.nio.file.Paths
import javax.xml.bind.JAXBContext
import javax.xml.stream.XMLEventReader

class StorageContentHandler1(private val reader: XMLEventReader) {
	private val context = JAXBContext.newInstance()
	private val unMarshaller = context.createUnmarshaller()

	fun run() = sequence {
		skipWhitespace()
		var event = reader.peek()
		while (event.isStartElement) {
			val element = unMarshaller.unmarshal(reader, Any::class.java)
			val node = element.value as Node

			if (node.localName != "image") {
				throw IllegalStateException("Found unexpected tag: ${node.localName}.")
			}

			val path = Paths.get(node.getElementsByTagName("path")[0].textContent)
			val id = node.attributes.getNamedItem("id").nodeValue.toUUID()
			val hash = Hash(
				node.getElementsByTagName("hash")[0].attributes.getNamedItem("type").nodeValue,
				node.getElementsByTagName("hash")[0].textContent
			)

			yield(TaggedFile(path, id, hash).also {
				it.tags.addAll(node.getElementsByTagName("tags")[0].textContent)
			})

			skipWhitespace()
			event = reader.peek()
		}
	}

	private fun skipWhitespace() {
		while (reader.peek().isCharacters && reader.peek().asCharacters().isWhiteSpace) reader.nextEvent()
	}
}
