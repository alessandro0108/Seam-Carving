package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage

class ImageProcessor {
    fun copyImage(outImage: BufferedImage, image: BufferedImage) {
        for (x in 0 until outImage.getWidth())
            for (y in 0 until outImage.getHeight()) {
                outImage.setRGB(x, y, image.getRGB(x, y))
            }
    }

    fun drawVerticalSeam(outImage: BufferedImage, seamArray: IntArray, color: Color) {
        for (y in 0 until outImage.getHeight()) {
            val x = seamArray[y]
            outImage.setRGB(x, y, color.rgb)
        }
    }

    fun drawHorizontalSeam(outImage: BufferedImage, seamArray: IntArray, color: Color) {
        for (x in 0 until outImage.getWidth()) {
            val y = seamArray[x]
            outImage.setRGB(x, y, color.rgb)
        }
    }

    private fun getNormalizedEnergyImage(outImage: BufferedImage, energyMatrix: Array<Array<EnergyPoint>>, maxEnergy: Double) {
        for (x in 0 until outImage.getWidth())
            for (y in 0 until outImage.getHeight()) {
                val energy: Double = energyMatrix[y][x].energy
                val intensity: Int = (255.0 * energy / maxEnergy).toInt()
                outImage.setRGB(x, y, Color(intensity, intensity, intensity).rgb)
            }
    }

    fun removeVerticalSeam(image: BufferedImage, seamArray: IntArray): BufferedImage {
        val outImage: BufferedImage = BufferedImage(image.width - 1, image.height, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until image.height) {
            var xSeamFound: Boolean = false
            for (x in 0 until image.width) {
                val xSeam = seamArray[y]
                if (x != xSeam) {
                    val newX = if (!xSeamFound) x else x - 1
                    outImage.setRGB(newX, y, image.getRGB(x, y))
                } else {
                    xSeamFound = true
                }
            }
        }
        return outImage
    }

    fun removeHorizontalSeam(image: BufferedImage, seamArray: IntArray): BufferedImage {
        val outImage: BufferedImage = BufferedImage(image.width, image.height-1, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until image.width) {
            var ySeamFound: Boolean = false
            for (y in 0 until image.height) {
                val ySeam = seamArray[x]
                if (y != ySeam) {
                    val newY = if (!ySeamFound) y else y - 1
                    outImage.setRGB(x, newY, image.getRGB(x, y))
                } else {
                    ySeamFound = true
                }
            }
        }
        return outImage
    }
}