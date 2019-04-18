package rocks.tiger.imagemanager.filetypes

import java.nio.file.Path

class CommonType : FileType {
	override val extensions = setOf("bmp", "png", "gif", "jpg", "jpeg", "jfif", "tif", "tiff", "wbmp")

	override fun getSize(path: Path): Size {
		TODO("not implemented")
	}

	override fun isAnimated(path: Path): Boolean {
		TODO("not implemented")
	}
}
