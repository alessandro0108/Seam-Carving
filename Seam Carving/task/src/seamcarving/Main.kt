package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    if (args.size != 8)
        throw Exception("Wrong number of parameters")
    checkParameterName(args[0], "-in")
    checkParameterName(args[2], "-out")
    checkParameterName(args[4], "-width")
    checkParameterName(args[6], "-height")
    val inputFileName = args[1]
    val outputFileName = args[3]
    val vertSeamsNum = args[5].toInt()
    val horizSeamsNum = args[7].toInt()

    val image: BufferedImage = ImageIO.read(File(inputFileName))
    var outImage: BufferedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    val energy = EnergyCalculator(image)
    energy.calcEnergyMatrix()
    //var maxEnergy: Double = energy.maxEnergy  // use in a previous stage of the project
    var energyMatrix = energy.energyMatrix

    val imgProc = ImageProcessor()
    imgProc.copyImage(outImage, image)

    repeat (vertSeamsNum) {
        val energy = EnergyCalculator(outImage)
        energy.calcEnergyMatrix()
        var energyMatrix = energy.energyMatrix
        val seamFinder = SeamFinder(energyMatrix)
        val seamArray = seamFinder.getVerticalSeam()
        outImage = imgProc.removeVerticalSeam(outImage, seamArray)
    }

    repeat (horizSeamsNum) {
        val energy = EnergyCalculator(outImage)
        energy.calcEnergyMatrix()
        var energyMatrix = energy.energyMatrix
        val seamFinder = SeamFinder(energyMatrix)
        val seamArray = seamFinder.getHorizontalSeam()
        outImage = imgProc.removeHorizontalSeam(outImage, seamArray)
    }

    ImageIO.write(outImage, "png", File(outputFileName))

}

private fun checkParameterName(actual: String, expected: String) {
    if (actual != expected)
        throw Exception("Unexpected parameter $actual")
}




