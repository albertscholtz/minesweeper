import kotlin.random.Random

fun main() {
    val list = MutableList(7) { Random(23).nextInt(20, 31) }
    print(list.joinToString(" "))
}