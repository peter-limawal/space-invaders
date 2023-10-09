import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.stage.Stage
import kotlin.system.exitProcess

const val MAX_WIDTH: Double = 1280.0
const val MAX_HEIGHT: Double = 720.0

var highScore: Int = 0

fun startGame(level: Int, stage: Stage) {
    val game = GameController(level, stage)
    with (stage) {
        scene = game.scene
    }
}

class SpaceInvaders : Application() {
    private val logo = ImageView(Image("images/logo.png"))
    private val instrBanner = Label("Instructions").apply {
        style = "-fx-font-size: 40px; -fx-font-weight: bold;"
    }
    private val instrComm = VBox().apply {
        alignment = Pos.CENTER
        style = "-fx-font-size: 20px; -fx-font-weight: bold;"

        val instrCommEnter = Label("ENTER - Start Game")
        val instrCommMove = Label("A or ◀, D or ▶ - Move ship left or right")
        val instrCommSpace = Label("SPACE - Fire!")
        val instrCommQuit = Label("Q - Quit Game")
        val instrCommLevel = Label("1 or 2 or 3 - Start Game at a specific level")

        children.addAll(instrCommEnter, instrCommMove, instrCommSpace, instrCommQuit, instrCommLevel)
    }
    private val studentID = Label("Implemented by Peter Limawal (20902549) for CS 349, University of Waterloo, S23")

    override fun start(stage: Stage) {
        val centrePane = VBox().apply {
            alignment = Pos.CENTER
            spacing = 30.0
            children.addAll(logo, instrBanner, instrComm)
        }

        val bottomPane = VBox().apply{
            alignment = Pos.CENTER
            prefHeight = 20.0
            children.add(studentID)
        }

        val root = BorderPane().apply {
            center = centrePane
            bottom = bottomPane
        }

        with (stage) {
            scene = Scene(root, MAX_WIDTH, MAX_HEIGHT)
            title = "A3 Space Invaders - Peter Limawal"
            isResizable = false
            show()

            scene.setOnKeyPressed {
                when (it.code) {
                    KeyCode.ENTER -> startGame(1, stage)
                    KeyCode.Q -> exitProcess(0)
                    KeyCode.DIGIT1 -> startGame(1, stage)
                    KeyCode.DIGIT2 -> startGame(2, stage)
                    KeyCode.DIGIT3 -> startGame(3, stage)
                    else -> {}
                }
            }
        }
    }
}