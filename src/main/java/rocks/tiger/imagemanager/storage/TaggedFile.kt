package rocks.tiger.imagemanager.storage

import rocks.tiger.imagemanager.tags.TaggedItem
import rocks.tiger.imagemanager.tags.Tags
import java.nio.file.Path
import java.util.*

class TaggedFile(val path: Path, val id: UUID, val hash: Hash) : TaggedItem()
