package seamcarving

class SeamFinder {
    val matrix: Array<Array<EnergyPoint>>
    private val matrixTransposed: Array<Array<EnergyPoint>>

    constructor(matrix: Array<Array<EnergyPoint>>) {
        this.matrix = matrix
        this.matrixTransposed = transpose(matrix)
    }

    private fun transpose(matrix: Array<Array<EnergyPoint>>): Array<Array<EnergyPoint>> {
        val tpMatrix = Array(matrix[0].size) { Array<EnergyPoint>(size = matrix.size) { EnergyPoint() } }
        for (i in matrix.indices)
            for (j in matrix[0].indices) {
                tpMatrix[j][i].energy = matrix[i][j].energy
            }
        return tpMatrix
    }

    fun getVerticalSeam(): IntArray {
        return getSeam(matrix)
    }

    fun getHorizontalSeam(): IntArray {
        return getSeam(matrixTransposed)
    }

    private fun getSeam(matrix: Array<Array<EnergyPoint>>): IntArray {
        val INF = Double.MAX_VALUE
        val workingMatrix = Array(matrix.size) { Array<EnergyPoint>(size = matrix[0].size) { EnergyPoint() } }

        // calculates the cumulative energy matrix: each pixel's energy is summed to the minimum
        // energy of the 3 pixels above it (coordinates j-1, j, j+1)
        for (i in matrix.indices)
            for (j in matrix[0].indices) {
                if (i == 0)
                    workingMatrix[i][j].energy = matrix[i][j].energy
                else {
                    val aboveEnergy = workingMatrix[i -1][j].energy
                    val aboveLeftEnergy = if (j > 0) workingMatrix[i - 1][j - 1].energy else INF
                    val aboveRightEnergy = if (j < workingMatrix[0].size - 1) workingMatrix[i - 1][j + 1].energy else INF

                    var minEnergy = aboveEnergy
                    var minJ = j
                    if (aboveLeftEnergy < minEnergy) {
                        minEnergy = aboveLeftEnergy
                        minJ = j - 1
                    }
                    if (aboveRightEnergy < minEnergy) {
                        minEnergy = aboveRightEnergy
                        minJ = j + 1
                    }
                    workingMatrix[i][j].energy = matrix[i][j].energy + minEnergy
                    workingMatrix[i][j].prevX = minJ
                }
            }

        // the lowest energy seam is the one with the minimum cumulative energy in the last row
        // of the matrix
        var minEnergy = INF
        var minJ = -1
        val lastRowIndex = workingMatrix.size - 1
        for (j in workingMatrix[0].indices) {
             if (workingMatrix[lastRowIndex][j].energy < minEnergy) {
                minJ = j
                minEnergy = workingMatrix[lastRowIndex][j].energy
            }
        }
//        println("minEnergy = $minEnergy; minJ = $minJ")

        val seamArray = IntArray(workingMatrix.size)
        seamArray[lastRowIndex] = minJ
        var seamEnergy = workingMatrix[lastRowIndex][minJ].energy

        for (i in lastRowIndex - 1 downTo 0) {
            seamArray[i] = workingMatrix[i + 1][seamArray[i + 1]].prevX
            seamEnergy += matrix[i][seamArray[i]].energy
        }

//        println("seamEnergy = $seamEnergy")

        return seamArray
    }
}