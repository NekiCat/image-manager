package rocks.tiger.imagemanager.filetypes

import java.nio.file.Path

interface FileType {
	val extensions: Iterable<String>

	fun getSize(path: Path): Size
	fun isAnimated(path: Path): Boolean
}
