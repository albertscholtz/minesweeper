class Block(val color: String) {

    object BlockProperties {
        var length: Int = 6
        var width: Int = 4

        fun blocksInBox(boxLength: Int, boxWidth: Int): Int {
            val fitInLength = boxLength / length
            val fitInWidth = boxWidth / width
            return fitInLength * fitInWidth
        }
    }
}