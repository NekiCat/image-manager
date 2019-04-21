package rocks.tiger.imagemanager.ui

import javafx.application.Platform
import tornadofx.*

class MainMenu : View() {
	override val root = menubar {
		menu("File") {
			item("Quit").action {
				Platform.exit()
			}
		}

		menu("About") {

		}
	}
}
