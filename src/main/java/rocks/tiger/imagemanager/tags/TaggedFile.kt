package rocks.tiger.imagemanager.tags

import java.nio.file.Path

class TaggedFile(val path: Path) : TaggedItem {
	override val tags = Tags(this)
}
