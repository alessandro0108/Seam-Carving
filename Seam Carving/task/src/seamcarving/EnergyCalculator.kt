package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage

class EnergyCalculator(val image: BufferedImage) {
    var maxEnergy: Double = 0.0
    val energyMatrix = Array(image.height) { Array<EnergyPoint>(size = image.width) { EnergyPoint() } }

    fun calcEnergyMatrix() {
        maxEnergy = 0.0

        for (x in 0 until image.getWidth())
            for (y in 0 until image.getHeight()) {
                val energy: Double = getEnergy(x, y)
                if (energy > maxEnergy) {
                    maxEnergy = energy
                }
                energyMatrix[y][x].energy = energy
            }
    }

    fun getTestEnergyMatrix1(): Array<Array<EnergyPoint>> {
        val test1e = arrayOf(
                arrayOf(5, 5, 3, 5),
                arrayOf(3, 3, 4, 4),
                arrayOf(4, 6, 0, 2),
                arrayOf(1, 5, 4, 0),
                arrayOf(3, 4, 4, 0),
        )       // seam: 3, 3, 0, 0, 0

        return createTestEnergyMatrix(test1e)
    }

    fun getTestEnergyMatrix2(): Array<Array<EnergyPoint>> {
        val test1e = arrayOf(
                arrayOf(1, 4, 3, 5, 2),
                arrayOf(3, 2, 5, 2, 3),
                arrayOf(5, 2, 4, 2, 1),
        )       // seam: 1, 2, 2

        return createTestEnergyMatrix(test1e)
    }

    private fun createTestEnergyMatrix(testMatrix: Array<Array<Int>>): Array<Array<EnergyPoint>> {
        val matrix = Array(testMatrix.size) { Array<EnergyPoint>(testMatrix[0].size) { EnergyPoint() } }

        for (i in testMatrix.indices)
            for (j in testMatrix[0].indices)
                matrix[i][j].energy = testMatrix[i][j].toDouble()

        return matrix
    }

    private fun getEnergy(x: Int, y: Int): Double {
        val gradX: Double = getGradientX(x, y)
        val gradY: Double = getGradientY(x, y)
        return Math.sqrt(gradX + gradY)
    }

    private fun getGradientX(x: Int, y: Int): Double {
        val x1 = adjustBorderX(x)
        val c1 = Color(image.getRGB(x1 + 1, y))
        val c2 = Color(image.getRGB(x1 - 1, y))
        return getSquareGradient(c1, c2)
    }

    private fun getGradientY(x: Int, y: Int): Double {
        val y1 = adjustBorderY(y)
        val c1 = Color(image.getRGB(x, y1 + 1))
        val c2 = Color(image.getRGB(x, y1 - 1))
        return getSquareGradient(c1, c2)
    }

    private fun adjustBorderX(x: Int): Int {
        return when (x) {
            0 -> x + 1;
            image.width - 1 -> x - 1
            else -> x
        }
    }

    private fun adjustBorderY(y: Int): Int {
        return when (y) {
            0 -> y + 1;
            image.height - 1 -> y - 1
            else -> y
        }
    }

    private fun getSquareGradient(c1: Color, c2: Color): Double {
        return Math.pow((c1.red - c2.red).toDouble(), 2.0) +
                Math.pow((c1.green - c2.green).toDouble(), 2.0) +
                Math.pow((c1.blue - c2.blue).toDouble(), 2.0)
    }



}