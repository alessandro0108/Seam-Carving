class Fabric(var color: String = "grey") {
    var pattern: String = "none"
    var patternColor: String = "none"
    init {
        println("$color fabric is created")
    }
    constructor(color: String, pattern: String, patternColor: String): this("grey") {
        this.color = color
        println("the fabric is dyed $color")
        this.pattern = pattern
        this.patternColor = patternColor
        println("$patternColor $pattern pattern is printed on the fabric")
    }
}