fun f(x: Double): Double {
    return when {
        x <= 0 -> f1(x)
        x > 0 && x < 1 -> f2(x)
        x >= 1 -> f3(x)
        else -> -1.0
    }
}

// implement your functions here
fun f1(x: Double): Double = Math.pow(x, 2.0) + 1

fun f2(x: Double): Double = 1.0 / Math.pow(x, 2.0)

fun f3(x: Double): Double = Math.pow(x, 2.0) - 1