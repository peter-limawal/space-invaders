import javafx.scene.image.Image
import javafx.scene.image.ImageView

val playerImg = Image("images/player.png")
const val playerWidth: Double = 80.0
val playerHeight: Double = (playerWidth / playerImg.width) * playerImg.height
class Player(private var x: Double, private var y: Double) {
    val playerView = ImageView(playerImg)

    fun getWidth() : Double {
        return playerView.fitWidth
    }

    fun getHeight() : Double {
        return playerView.fitHeight
    }

    fun getXPos() : Double {
        return playerView.layoutX
    }

    fun getYPos() : Double {
        return playerView.layoutY
    }

    init {
        playerView.layoutX = x
        playerView.layoutY = y
        playerView.fitWidth = playerWidth
        playerView.isPreserveRatio = true
    }
}