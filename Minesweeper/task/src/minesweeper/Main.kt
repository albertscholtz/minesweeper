package minesweeper

fun String.isInt(): Boolean {
    return this.all { char -> char.isDigit() }
}

enum class Command {
    FREE, MINE;

    companion object {
        fun isValid(opt: String) = values().any { command -> command.name == opt.uppercase() }
    }

    fun isFree() = this == FREE
    fun isMine() = this == MINE
}

class Coords(var value: String) {

    private var mine: Boolean = false
    private var explored: Boolean = false

    companion object {
        const val EMPTY: String = "."
        const val EXPLORED: String = "/"
        const val MINE: String = "X"

        const val MARKED: String = "*"
    }

    fun isMine(): Boolean = mine
    fun isExplored(): Boolean = explored
    fun isMarked(): Boolean = value == MARKED

    fun isEmpty(): Boolean = value == EMPTY
    fun isHinted() = !isEmpty() && !isMarked() && !isMine() && !isExplored()
    fun toggleMark() = if (isMarked()) value = EMPTY else value = MARKED
    fun mine() {
        value = MINE
        this.mine = true
    }

    fun clear() {
        value = EXPLORED
        explore()
    }

    fun explore() {
        explored = true
    }
}


object Grid {
    var gridSideLength: Int = 0
    val grid = mutableListOf(mutableListOf<Coords>())

    fun initalize(gridSideLength: Int, amountOfMines: Int) {
        this.gridSideLength = gridSideLength
        resetGame(gridSideLength)
        populateGame(gridSideLength, amountOfMines)
    }

    fun clearCellAndNeighbours(row: Int, column: Int) {
        val maxIndex = gridSideLength - 1
        if (row > maxIndex || row < 0) return
        if (column > maxIndex || column < 0) return

        val cell = grid[row][column]
        if (cell.isExplored()) {
            return
        }

        if (cell.isMine()) return

        if (cell.isHinted()) {
            cell.explore()
            return
        }

        cell.clear()

        clearCellAndNeighbours(row - 1, column - 1)
        clearCellAndNeighbours(row - 1, column)
        clearCellAndNeighbours(row - 1, column + 1)
        clearCellAndNeighbours(row, column - 1)
        clearCellAndNeighbours(row, column + 1)
        clearCellAndNeighbours(row + 1, column - 1)
        clearCellAndNeighbours(row + 1, column)
        clearCellAndNeighbours(row + 1, column + 1)
    }

    fun allMinesMarked(): Boolean {
        grid.forEach { list ->
            list.forEach { coord ->
                if (coord.isMine() && !coord.isMarked()) {
                    return true
                } else if (coord.isMarked() && !coord.isMine()) {
                    return true
                }
            }
        }
        println("Congratulations! You found all the mines!")
        return false
    }

    private fun resetGame(gridSideLength: Int) {
        grid.clear()
        for (row in 0 until gridSideLength) {
            grid.add(MutableList(9) {
                Coords(Coords.EMPTY)
            })
        }
    }

    private fun populateGame(gridSideLength: Int, amountOfMines: Int) {
        val squares = gridSideLength * gridSideLength
        val mineSquareNumbers = MutableList(amountOfMines) { (0 until squares).random() }

        for (square in 0 until squares) {
            val row = square / gridSideLength
            val column = square % gridSideLength
            if (mineSquareNumbers.contains(square)) {
                grid[row][column].mine()
                determineHints(row, column, gridSideLength)
            }
        }
    }

    private fun determineHints(row: Int, column: Int, gridSideLength: Int) {
        incrementCellHint(row - 1, column - 1, gridSideLength)
        incrementCellHint(row - 1, column, gridSideLength)
        incrementCellHint(row - 1, column + 1, gridSideLength)
        incrementCellHint(row, column - 1, gridSideLength)
        incrementCellHint(row, column + 1, gridSideLength)
        incrementCellHint(row + 1, column - 1, gridSideLength)
        incrementCellHint(row + 1, column, gridSideLength)
        incrementCellHint(row + 1, column + 1, gridSideLength)
    }

    private fun incrementCellHint(targetRow: Int, targetColumn: Int, gridSideLength: Int) {
        val maxIndex = gridSideLength - 1
        if (targetRow > maxIndex || targetRow < 0) return
        if (targetColumn > maxIndex || targetColumn < 0) return

        val current = grid[targetRow][targetColumn]
        if (current.isMine()) return

        if (current.value == ".") {
            current.value = "1"
        } else {
            current.value = current.value.toInt().plus(1).toString()
        }
    }
}

private fun printGrid() {
    val gridSideLength = Grid.gridSideLength
    println(" |${(1..gridSideLength).joinToString("")}|")
    println("—|${(1..gridSideLength).joinToString("") { "—" }}|")

    var rowCount = 1;
    for (row in Grid.grid) {
        print("$rowCount|")

        for (column in row) {
            if (column.isExplored() || column.isMarked() || column.isHinted()) print(column.value) else print(Coords.EMPTY)
        }

        println("|")
        rowCount++
    }
    println("-|${(1..gridSideLength).joinToString("") { "—" }}|")
}

fun markCell(x: Int, y: Int) {
    val markX = x - 1
    val markY = y - 1
    val cell = Grid.grid[markX][markY]
    if (cell.isHinted()) {
        println("There is a number here! ${cell.value}")
        return
    }
    if (cell.isExplored()){
        return
    }

    cell.toggleMark()
}

fun markFree(x: Int, y: Int): Boolean {
    val markX = x - 1
    val markY = y - 1

    val cell = Grid.grid[markX][markY]

    if (cell.isMine()) {
        cell.explore()
        printGrid()
        println("You stepped on a mine and failed!")
        return false
    }

    if (cell.isHinted()) {
        cell.explore()
        return true
    }

    if (cell.isEmpty()) {
        Grid.clearCellAndNeighbours(markX, markY)
        return true
    }


    return true
}

private fun coordInput(): Triple<Int, Int, Command> {
    var triple = Triple(-1, -1, Command.FREE)
    var invalid = true
    while (invalid) {
        println("Set/unset mines marks or claim a cell as free: ")
        val coords = readln()
        val (y, x, z) = coords.split(" ")
        invalid = false
        if (x.isBlank() || y.isBlank() || z.isBlank()) {
            println("Error! Please enter coordinates as two digits with free / mine as options, all with a space in between.")
            invalid = true
        }
        if (!x.isInt() || !y.isInt()) {
            println("Error! Please enter coordinates as two digits with a space in between.")
            invalid = true
        }
        if (!Command.isValid(z)) {
            println("Error! Please enter a valid command - free / mark.")
            invalid = true

        }
        if (x.toInt() > Grid.gridSideLength || y.toInt() > Grid.gridSideLength) {
            println("Error! Coordinates are outside of the grid")
            invalid = true
        }

        if (!invalid) {
            triple = Triple(x.toInt(), y.toInt(), Command.valueOf(z.uppercase()))
        }
    }
    return triple
}

private fun playGame() {
    var continueGame = true
    while (continueGame) {
        printGrid()
        val (x, y, command) = coordInput()
        if (command.isMine()) markCell(x, y) else {
            continueGame = markFree(x, y)
        }
        continueGame = continueGame && Grid.allMinesMarked()
    }
}

private fun restartGame() {
    println("How many mines do you want on the field? ")
    val gridSideLength = 9
    val amountOfMines = readln().toInt()

    Grid.initalize(gridSideLength, amountOfMines)
}

fun main() {
    restartGame()
    playGame()
}
