import javafx.scene.image.Image
import javafx.scene.image.ImageView

val bulletImg1 = Image("images/bullet1.png")
val bulletImg2 = Image("images/bullet2.png")
val bulletImg3 = Image("images/bullet3.png")
const val bulletWidth: Double = 20.0
const val bulletSpeed: Double = 3.0
class EnemyBullet(val enemyType: Int, private var x: Double, private var y: Double) {
    var bulletView = ImageView(bulletImg1)

    fun getWidth() : Double {
        return bulletView.fitWidth
    }

    fun getHeight() : Double {
        return bulletView.fitHeight
    }

    fun getXPos() : Double {
        return bulletView.layoutX
    }

    fun getYPos() : Double {
        return bulletView.layoutY
    }

    fun fire(currLevel: Int) {
        y += bulletSpeed * (1 + (currLevel / 3))
        bulletView.layoutY = y
    }

    init {
        when (enemyType) {
            1 -> bulletView = ImageView(bulletImg1)
            2 -> bulletView = ImageView(bulletImg2)
            3 -> bulletView = ImageView(bulletImg3)
            else -> {}
        }

        bulletView.layoutX = x
        bulletView.layoutY = y
        bulletView.fitWidth = bulletWidth
        bulletView.isPreserveRatio = true
    }
}