package rocks.tiger.imagemanager.ui

import tornadofx.*

class MainView : View() {
	override val root = borderpane {
		top<MainMenu>()

		left = vbox {
			button("Search")
		}

		center = flowpane {
			label("Hello World")
		}
	}
}
