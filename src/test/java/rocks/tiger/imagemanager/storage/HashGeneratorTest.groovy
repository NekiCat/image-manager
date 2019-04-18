package rocks.tiger.imagemanager.storage

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.StandardOpenOption

class HashGeneratorTest extends Specification {
	def "should create md5 hash"() {
		given:
		def generator = new HashGenerator()
		def fs = Jimfs.newFileSystem(Configuration.unix())
		def path = fs.getPath("/test.xml")
		Files.write(path, "Hello World".getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)

		when:
		def hash = generator.generate(path, "md5")

		then:
		hash.type == "md5"
		hash.value == "b10a8db164e0754105b7a99be72e3fe5"
	}

	def "should create sha1 hash"() {
		given:
		def generator = new HashGenerator()
		def fs = Jimfs.newFileSystem(Configuration.unix())
		def path = fs.getPath("/test.xml")
		Files.write(path, "Hello World".getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)

		when:
		def hash = generator.generate(path, "sha1")

		then:
		hash.type == "sha1"
		hash.value == "0a4d55a8d778e5022fab701977c5d840bbc486d0"
	}
}
