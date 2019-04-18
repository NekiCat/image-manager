package rocks.tiger.imagemanager

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

fun String.toUUID() = UUID.fromString(this)

fun Path.getExtension(): String {
	if (Files.isDirectory(this)) {
		return ""
	}

	val name = this.fileName.toString()
	val index = name.lastIndexOf('.')
	if (index < 0) {
		return ""
	}

	return name.substring(index + 1)
}

inline fun NodeList.forEach(action: (Node) -> Unit) {
	val lastIndex = this.length - 1
	for (i in 0..lastIndex) {
		action(this.item(i))
	}
}

fun NodeList.toList(): List<Node> {
	return object : AbstractList<Node>() {
		override val size = length
		override fun get(index: Int) = item(index)
	}
}

fun Node.getElementsByTagName(name: String): List<Node> {
	return childNodes.toList().filter {
		node -> node.nodeName == name
	}
}
