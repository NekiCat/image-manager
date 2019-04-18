package rocks.tiger.imagemanager.storage

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

class HashGenerator {
	fun generate(path: Path, type: String): Hash {
		val messageDigest = MessageDigest.getInstance(type)
		Files.newInputStream(path, StandardOpenOption.READ).use { stream ->
			val buffer = ByteArray(1024 * 32)
			while (true) {
				val read = stream.read(buffer)
				if (read <= 0) {
					break
				}

				messageDigest.update(buffer, 0, read)
			}
		}

		return Hash(type, DatatypeConverter.printHexBinary(messageDigest.digest()).toLowerCase())
	}
}
