data class Car(val number: String, val color: String) {
    override fun toString() = "$number $color"
}

class Parking(size: Int) {
    private val spots = Array<Car?>(size) { null }

    fun useSpot(spot: Int, car: Car) {
        spots[spot] = car
    }

    fun freeSpot(spot: Int) {
        spots[spot] = null
    }

    fun isFree(spot: Int) = spots[spot] == null
    fun findFreeSpot() = spots.indexOfFirst { it == null }
    fun usedSpots() = spots.withIndex().filter { it.value != null }

    fun spotsByColor(color: String) = spots.mapIndexedNotNull { index, car ->
        index.takeIf { car?.color?.equals(color, true) == true }
    }

    fun spotByNumber(number: String) = spots.indexOfFirst { it?.number == number }

    fun getCar(spot: Int) = spots[spot]
}

fun main() {
    var parking: Parking? = null

    while (true) {
        val input = readLine()!!.split(" ")
        val command = input[0]
        if (command == "exit") break
        else if (command == "create") {
            val size = input[1].toInt()
            parking = Parking(size)
            println("Created a parking lot with $size spots.")
        } else if (parking == null) println("Sorry, a parking lot has not been created.")
        else when (command) {
            "park" -> {
                val spot = parking.findFreeSpot()
                if (spot == -1) {
                    println("Sorry, the parking lot is full.")
                } else {
                    val (_, number, color) = input
                    parking.useSpot(spot, Car(number, color))
                    println("$color car parked in spot ${spot + 1}.")
                }
            }
            "leave" -> {
                val spot = input[1].toInt()
                if (parking.isFree(spot - 1)) {
                    println("There is no car in spot $spot.")
                } else {
                    parking.freeSpot(spot - 1)
                    println("Spot $spot is free.")
                }
            }
            "status" -> {
                val used = parking.usedSpots()
                if (used.isEmpty()) println("Parking lot is empty.")
                else used.forEach { (spot, car) -> println("${spot + 1} $car") }
            }
            "reg_by_color" -> {
                val color = input[1]
                val spots = parking.spotsByColor(color)
                if (spots.isEmpty()) {
                    println("No cars with color $color were found.")
                } else {
                    println(spots.joinToString { parking.getCar(it)!!.number })
                }
            }
            "spot_by_color" -> {
                val color = input[1]
                val spots = parking.spotsByColor(color)
                if (spots.isEmpty()) {
                    println("No cars with color $color were found.")
                } else {
                    println(spots.joinToString { (it + 1).toString() })
                }
            }
            "spot_by_reg" -> {
                val number = input[1]
                val spot = parking.spotByNumber(number)
                if (spot == -1) {
                    println("No cars with registration number $number were found.")
                } else {
                    println(spot + 1)
                }
            }
        }
    }
}
