package rocks.tiger.imagemanager.storage

import rocks.tiger.imagemanager.storage.handler.StorageContentHandler1
import rocks.tiger.imagemanager.tags.ItemPool
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.events.Attribute

class FileStorage {
	private val inputFactory = XMLInputFactory.newInstance()
	private val outputFactory = XMLOutputFactory.newInstance()

	fun loadFromStream(stream: InputStream): ItemPool {
		val result = ItemPool()

		val reader = inputFactory.createXMLEventReader(stream)
		val root = reader.nextTag()

		val handler = when (val version = root.asStartElement().attributes.let {
			for (attribute in it) {
				if ((attribute as Attribute).name.toString() == "version") {
					return@let attribute.value
				}
			}
		}) {
			"1" -> StorageContentHandler1(reader)
			else -> throw IllegalStateException("XML version $version is not supported")
		}

		handler.run().forEach {
			result.add(it)
		}

		return result
	}

	fun loadFromFile(path: Path): ItemPool {
		Files.newInputStream(path, StandardOpenOption.READ).use {
			return loadFromStream(it)
		}
	}

	fun saveToStream(stream: OutputStream, items: ItemPool) {
		val writer = outputFactory.createXMLStreamWriter(OutputStreamWriter(stream, "utf-8"))

		writer.writeStartDocument("utf-8", "1.0")
		writer.writeStartElement("images")
		writer.writeAttribute("version", "1")

		for (item in items) {
			if (item !is TaggedFile) {
				continue
			}

			writer.writeStartElement("image")
			writer.writeAttribute("id", item.id.toString())

			writer.writeStartElement("hash")
			writer.writeAttribute("type", item.hash.type)
			writer.writeCharacters(item.hash.value)
			writer.writeEndElement()

			writer.writeStartElement("path")
			writer.writeCharacters(item.path.toString())
			writer.writeEndElement()

			writer.writeStartElement("tags")
			writer.writeCharacters(item.tags.toString())
			writer.writeEndElement()

			writer.writeEndElement()
		}

		writer.writeEndElement()
		writer.close()
	}

	fun saveToFile(path: Path, items: ItemPool) {
		Files.newOutputStream(path).use {
			saveToStream(it, items)
		}
	}
}
