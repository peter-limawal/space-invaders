import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlin.random.Random

val enemyImg1 = Image("images/enemy1.png")
val enemyImg2 = Image("images/enemy2.png")
val enemyImg3 = Image("images/enemy3.png")
const val enemyWidth: Double = 50.0
const val enemyHeight: Double = 40.0
const val enemyMoveDownSpeed: Double = 20.0
class EnemyShip(val enemyType : Int, private var x : Double, private var y : Double) {
    var shipView = ImageView(enemyImg1)

    fun getWidth() : Double {
        return shipView.fitWidth
    }

    fun getHeight() : Double {
        return shipView.fitHeight
    }

    fun getXPos() : Double {
        return shipView.layoutX
    }

    fun getYPos() : Double {
        return shipView.layoutY
    }

    init {
        when (enemyType) {
            1 -> shipView = ImageView(enemyImg1)
            2 -> shipView = ImageView(enemyImg2)
            3 -> shipView = ImageView(enemyImg3)
            else -> {}
        }

        shipView.layoutX = x
        shipView.layoutY = y
        shipView.fitWidth = enemyWidth
        shipView.fitHeight = enemyHeight
    }

    fun shift(xDelta : Double, yDelta : Double) {
        shipView.layoutX += xDelta
        shipView.layoutY += yDelta
    }

    fun leftBound() : Boolean {
        return shipView.layoutX < 0
    }

    fun rightBound() : Boolean {
        return shipView.layoutX + enemyWidth > MAX_WIDTH
    }

    fun hitPlayer(player: Player) : Boolean {
        return (shipView.layoutY + enemyHeight >= player.getYPos()
                && shipView.layoutX >= player.getXPos()
                && shipView.layoutX <= player.getXPos() + player.getWidth())
    }

    fun bottomBound() : Boolean {
        return shipView.layoutY + enemyHeight >= GAME_HEIGHT
    }
}

class Enemy(var speed : Double, var numRows : Int, var numCols : Int, var x : Double, var y : Double) {
    val ships: ArrayList<ArrayList<EnemyShip>> = ArrayList()
    init {
        for (r in 0 until numRows) {
            val row: ArrayList<EnemyShip> = ArrayList()
            for (c in 0 until numCols) {
                var type: Int = 1
                when (r) {
                    0 -> type = 3
                    1,2 -> type = 2
                    3,4 -> type = 1
                }
                row.add(EnemyShip(type, c * (enemyWidth + 10) + x, r * (enemyHeight + 10) + y))
            }
            ships.add(row)
        }
    }

    fun pickRandom() : EnemyShip {
        return ships.random().random()
    }

    fun speedUp () {
        if (speed > 0) speed += 0.1
        else speed -= 0.1
    }

    fun move(player: Player, game: Pane) {
        for (row in ships) {
            if (row.isEmpty()) continue
            for (ship in row) {
                if (ship.bottomBound()) {
                    MediaPlayer(explosionSound).play()
                    gameOver = true
                    livesCount = 0
                }
                if (ship.hitPlayer(player)) {
                    MediaPlayer(explosionSound).play()
                    alienHit = true
                    game.children.remove(ship.shipView)
                    row.remove(ship)
                    speedUp()
                }
            }
            if (row.last().rightBound() || row.first().leftBound()) {
                MediaPlayer(fastInvader4Sound).play()
                speed *= -1
                ships.forEach {row ->
                    row.forEach {ship -> ship.shift(0.0, enemyMoveDownSpeed) }
                }
                contact = true
                break
            }
        }

        ships.forEach {row ->
            row.forEach {ship -> ship.shift(speed, 0.0)}
        }
    }
}