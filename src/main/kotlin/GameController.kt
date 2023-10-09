import javafx.animation.AnimationTimer
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File
import kotlin.random.Random
import kotlin.system.exitProcess

interface AnimatableScene {
    var x: Double
    var y: Double
    fun draw() { }
}

var timer: AnimationTimer? = null

fun animationTimer(aniScene: AnimatableScene) {
    // create timer using JavaFX 60FPS execution thread
    timer = object : AnimationTimer() {
        override fun handle(now: Long) {
            aniScene.x += 1.0
            aniScene.y += 1.0
            aniScene.draw()
        }
    }
    // start timer
    timer?.start()
}

const val STATUS_HEIGHT = 40.0
val GAME_HEIGHT = MAX_HEIGHT - STATUS_HEIGHT
val playerStartX = (MAX_WIDTH - playerWidth) / 2
val playerStartY = GAME_HEIGHT - playerHeight
val enemyStartX = (MAX_WIDTH - (10 * (enemyWidth + 10))) / 2
const val enemyStartY = 30.0
const val NUM_ROWS = 5
const val NUM_COLS = 10

const val playerMoveSpeed = 20.0
var gameOver: Boolean = false
var contact: Boolean = false
var livesCount: Int = 3
var scoreCount: Int = 0
var playerCanFire: Boolean = true
var alienHit: Boolean = false

val explosionSound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/explosion.wav").toURI().toString())
val fastInvader1Sound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/fastinvader1.wav").toURI().toString())
val fastInvader2Sound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/fastinvader2.wav").toURI().toString())
val fastInvader3Sound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/fastinvader3.wav").toURI().toString())
val fastInvader4Sound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/fastinvader4.wav").toURI().toString())
val invaderKilledSound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/invaderkilled.wav").toURI().toString())
val shootSound = Media(File("${System.getProperty("user.dir")}/src/main/resources/sounds/shoot.wav").toURI().toString())

fun reset() {
    gameOver = false
    contact = false
    livesCount = 3
    scoreCount = 0
    playerCanFire = true
    alienHit = false
}

fun levelUp() {
    gameOver = false
    contact = false
    playerCanFire = true
    alienHit = false
}

class GameController(private var currLevel: Int, private var stage: Stage) : AnimatableScene {

    private val status = HBox()
    private val game = Pane()
    private val root = BorderPane()

    var scene = Scene(root, MAX_WIDTH, MAX_HEIGHT)

    private var player = Player(playerStartX, playerStartY)
    private var enemy = Enemy(speed = 1.0, numRows = NUM_ROWS, numCols = NUM_COLS, x = enemyStartX, y = enemyStartY)

    override var x: Double = 0.0
    override var y: Double = 0.0

    private var enemyBullets: MutableList<EnemyBullet> = mutableListOf()
    private var playerBullets: MutableList<PlayerBullet> = mutableListOf()

    private fun enemyFire() {
        val firingShip = enemy.pickRandom()
        val xBullet = firingShip.shipView.layoutX + (firingShip.shipView.fitWidth / 2)
        val yBullet = firingShip.shipView.layoutY + firingShip.shipView.fitHeight
        val newBullet = EnemyBullet(firingShip.enemyType, xBullet, yBullet)
        game.children.add(newBullet.bulletView)
        enemyBullets.add(newBullet)
        when (firingShip.enemyType) {
            1 -> MediaPlayer(fastInvader1Sound).play()
            2 -> MediaPlayer(fastInvader2Sound).play()
            3 -> MediaPlayer(fastInvader3Sound).play()
            else -> {}
        }
    }

    private fun playerFire() {
        if (playerCanFire) {
            playerCanFire = false
            val xPlayerBullet = player.playerView.layoutX + (player.playerView.fitWidth / 2)
            val yPlayerBullet = player.playerView.layoutY
            val newPlayerBullet = PlayerBullet(xPlayerBullet, yPlayerBullet)
            game.children.add(newPlayerBullet.playerBulletView)
            playerBullets.add(newPlayerBullet)
            MediaPlayer(shootSound).play()

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    playerCanFire = true
                }
            }, 500)
        }
    }

    private fun updateStatus() {
        status.children.clear()

        val scoreCountLabel = Label("Score: $scoreCount").apply { style = "-fx-text-fill: white;" }
        val currLevelLabel = Label("Level: $currLevel").apply { style = "-fx-text-fill: white;" }
        val livesCountLabel = Label("Lives: $livesCount").apply { style = "-fx-text-fill: white;" }

        status.children.addAll(scoreCountLabel, currLevelLabel, livesCountLabel)
    }

    private fun respawnPlayer() {
        game.children.remove(player.playerView)
        val playerNewX = Random.nextDouble(0.0, MAX_WIDTH - player.getWidth())
        player = Player(playerNewX, playerStartY)
        game.children.add(player.playerView)
    }

    private fun clearPlayerBullet(bullet: PlayerBullet) {
        game.children.remove(bullet.playerBulletView)
        playerBullets.remove(bullet)
    }

    private fun clearEnemyBullet(bullet: EnemyBullet) {
        game.children.remove(bullet.bulletView)
        enemyBullets.remove(bullet)
    }

    private fun endGame(message: Label) : VBox {
        val scoreLabel = Label("Your Score: $scoreCount").apply {
            style = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;"
            alignment = Pos.CENTER
        }
        if (scoreCount > highScore) highScore = scoreCount
        val highScoreLabel = Label("High Score: $highScore").apply {
            style = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;"
            alignment = Pos.CENTER
        }
        val scoreBox = HBox().apply {
            alignment = Pos.CENTER
            spacing = 30.0
            children.addAll(scoreLabel, highScoreLabel)
        }

        val restartInstr = Label("Press 'R' to restart").apply {
            style = "-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: white;"
            alignment = Pos.CENTER
        }
        val quitInstr = Label("Press 'Q' to quit").apply {
            style = "-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: white;"
            alignment = Pos.CENTER
        }
        val endGameScreen = VBox().apply {
            alignment = Pos.CENTER
            spacing = 30.0
            prefWidth = MAX_WIDTH
            prefHeight = GAME_HEIGHT

            children.addAll(message, scoreBox, restartInstr, quitInstr)
        }

        return endGameScreen
    }

    init {
        animationTimer(this)
        var speed = 1.0
        when (currLevel) {
            1 -> speed = 1.0
            2 -> speed = 2.0
            3 -> speed = 3.0
            else -> {}
        }

        enemy = Enemy(speed = speed, numRows = NUM_ROWS, numCols = NUM_COLS, x = enemyStartX, y = enemyStartY)
//        enemy = Enemy(speed = speed, numRows = 1, numCols = 1, x = enemyStartX, y = enemyStartY)

        status.apply {
            alignment = Pos.CENTER
            style = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-background-color: black;"
            prefHeight = STATUS_HEIGHT
            spacing = 100.0
        }

        updateStatus()

        game.apply {
            style = "-fx-background-color: black;"
            children.add(player.playerView)
            for (row in enemy.ships) {
                for (ship in row) {
                    children.add(ship.shipView)
                }
            }
        }

        root.apply {
            top = status
            center = game
        }

        scene.setOnKeyPressed {
            when (it.code) {
                KeyCode.A, KeyCode.LEFT -> {
                    if (!gameOver && player.getXPos() - playerMoveSpeed >= 0) {
                        player.playerView.layoutX -= playerMoveSpeed
                    }
                }
                KeyCode.D, KeyCode.RIGHT -> {
                    if (!gameOver && player.getXPos() + playerMoveSpeed <= MAX_WIDTH - player.getWidth()) {
                        player.playerView.layoutX += playerMoveSpeed
                    }
                }
                KeyCode.SPACE -> {
                    if (!gameOver) {
                        playerFire()
                    }
                }
                KeyCode.R -> {
                    if (gameOver) {
                        currLevel = 1
                        reset()
                        startGame(currLevel, stage)
                    }
                }
                KeyCode.Q -> if (gameOver) exitProcess(0)
                else -> {}
            }
        }
    }

    override fun draw() {
        if (!gameOver) {
            val enemyCount: Int = enemy.ships.flatten().count()
            if (enemyCount == 0) gameOver = true

            enemy.move(player, game)
            if (alienHit) {
                livesCount -= 1
                updateStatus()
                if (livesCount == 0) {
                    gameOver = true
                    return
                }
                respawnPlayer()
                alienHit = false
            }

            if (contact) {
                enemyFire()
                contact = false
            }

            if (enemyBullets.size < (5 + currLevel) && Random.nextDouble(0.0, (200.0 + enemyCount) / currLevel) <= 1.0) {
                enemyFire()
            }

            enemyBullets.forEach {bullet ->
                bullet.fire(currLevel)
                val xBullet: Double = bullet.getXPos()
                val yBullet: Double = bullet.getYPos()
                val bulletWidth: Double = bullet.getWidth()
                val bulletHeight: Double = bullet.getHeight()
                val xPlayer: Double = player.getXPos()
                val yPlayer: Double = player.getYPos()
                // enemy bullet hits player
                if (xBullet >= xPlayer
                    && xBullet - bulletWidth <= xPlayer + playerWidth
                    && yBullet + bulletHeight >= yPlayer) {
                    MediaPlayer(explosionSound).play()
                    clearEnemyBullet(bullet)

                    livesCount -= 1
                    updateStatus()
                    if (livesCount == 0) {
                        gameOver = true
                        return
                    }
                    respawnPlayer()
                }
                // enemy bullet hits floor
                if (yBullet + bulletHeight > GAME_HEIGHT) clearEnemyBullet(bullet)
            }

            playerBullets.forEach {bullet ->
                bullet.fire()
                val xPlayerBullet: Double = bullet.playerBulletView.layoutX
                val yPlayerBullet: Double = bullet.playerBulletView.layoutY
                // player bullet hits enemy
                enemy.ships.forEach {row ->
                    row.forEach {ship ->
                        val xShip = ship.shipView.layoutX
                        val yShip = ship.shipView.layoutY
                        if (xPlayerBullet >= xShip
                            && xPlayerBullet - playerBulletWidth <= xShip + enemyWidth
                            && yPlayerBullet <= yShip + enemyHeight) {
                            MediaPlayer(invaderKilledSound).play()
                            clearPlayerBullet(bullet)
                            game.children.remove(ship.shipView)
                            row.remove(ship)

                            enemy.speedUp()
                            scoreCount += ship.enemyType * 10 * currLevel
                            updateStatus()
                        }
                    }
                }
                // player bullet hits ceil
                if (yPlayerBullet < 0) clearPlayerBullet(bullet)
            }
        } else {
            timer?.stop()
            // win
            if (livesCount > 0) {
                if (currLevel < 3) {
                    levelUp()
                    ++currLevel
                    startGame(currLevel, stage)
                } else {
                    val youWinLabel = Label("You Win").apply {
                        style = "-fx-font-size: 200px; -fx-font-weight: bold; -fx-text-fill: white;"
                        alignment = Pos.CENTER
                    }

                    game.children.add(endGame(youWinLabel))
                }
            }
            // lose
            else {
                val youLoseLabel = Label("You Lose").apply {
                    style = "-fx-font-size: 200px; -fx-font-weight: bold; -fx-text-fill: white;"
                    alignment = Pos.CENTER
                }

                game.children.add(endGame(youLoseLabel))
            }
        }
    }
}