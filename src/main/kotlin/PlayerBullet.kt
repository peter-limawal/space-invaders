import javafx.scene.image.Image
import javafx.scene.image.ImageView

val playerBulletImg = Image("images/player_bullet.png")
const val playerBulletWidth: Double = 10.0
const val playerBulletSpeed: Double = 5.0
class PlayerBullet(private var x: Double, private var y: Double) {
    val playerBulletView = ImageView(playerBulletImg)

    fun getWidth() : Double {
        return playerBulletView.fitWidth
    }

    fun getHeight() : Double {
        return playerBulletView.fitHeight
    }

    fun getXPos() : Double {
        return playerBulletView.layoutX
    }

    fun getYPos() : Double {
        return playerBulletView.layoutY
    }

    fun fire() {
        y -= playerBulletSpeed
        playerBulletView.layoutY = y
    }

    init {
        playerBulletView.layoutX = x
        playerBulletView.layoutY = y
        playerBulletView.fitWidth = playerBulletWidth
        playerBulletView.isPreserveRatio = true
    }
}