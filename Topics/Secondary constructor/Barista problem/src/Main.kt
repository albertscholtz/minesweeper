class EspressoMachine {
    val costPerServing: Float

    constructor(coffeeCapsulesCount: Int, totalCost: Float) {
        costPerServing = totalCost / coffeeCapsulesCount
    }

    constructor(coffeeBeansWeight: Float, totalCost: Float) {
        costPerServing = totalCost / (coffeeBeansWeight / 10)
        // cost = R300 / 300g / 10g
    }
}