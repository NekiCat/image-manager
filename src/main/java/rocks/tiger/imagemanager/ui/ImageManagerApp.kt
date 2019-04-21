package rocks.tiger.imagemanager.ui

import tornadofx.App
import tornadofx.launch

fun main(args: Array<String>) = launch<ImageManagerApp>(args)

class ImageManagerApp : App(MainView::class)
