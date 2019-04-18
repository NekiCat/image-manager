package rocks.tiger.imagemanager.filetypes

import org.reflections.Reflections

class FileTypes {
	private val instances: MutableMap<Class<out FileType>, FileType> = HashMap()
	private val extensionMap: MutableMap<String, FileType> = HashMap()

	init {
	    val fileTypeClasses = Reflections("rocks.tiger.imagemanager").getSubTypesOf(FileType::class.java)
		for (cls in fileTypeClasses) {
			val ctr = cls.getConstructor()
			val instance = ctr.newInstance()
			instances[cls] = instance

			for (ext in instance.extensions) {
				extensionMap[ext.toLowerCase()] = instance
			}
		}
	}

	fun getExtensions(): Iterable<String> = extensionMap.keys

	fun getExtensionsGlob(): String = "glob:**.{${extensionMap.keys.joinToString(",")}}"

	fun hasFileType(extension: String): Boolean = extensionMap.containsKey(extension.toLowerCase())

	fun getFileType(extension: String): FileType = extensionMap[extension.toLowerCase()]!!
}
