class Cat(val name: String, val color: String) {
    val some: Int = 1

    inner class Bow(val color: String) {
        fun printColor() {
            println("The cat named ${this@Cat.name} is ${this@Cat.color} and has a $color bow.")
            println(some)
        }
    }
}

fun main() {
    val cat: Cat = Cat("Princess", "green")
    val bow: Cat.Bow = cat.Bow("golden")

    bow.printColor()
}