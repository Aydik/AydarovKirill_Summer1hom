package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numCarsEditText: EditText = findViewById(R.id.numCarsEditText)
        val startRaceButton: Button = findViewById(R.id.startRaceButton)
        val raceResultsTextView: TextView = findViewById(R.id.raceResultsTextView)

        startRaceButton.setOnClickListener {
            val numCars = numCarsEditText.text.toString().toIntOrNull()
            if (numCars != null && numCars > 0) {
                val cars = mutableListOf<Car>()
                repeat(numCars) {
                    cars.add(createRandomCar())
                }

                val raceResults = StringBuilder()
                race(cars, raceResults)
                raceResultsTextView.text = raceResults.toString()
            } else {
                raceResultsTextView.text = "Введите корректное количество автомобилей"
            }
        }
    }
}

open class Car(val mark: String, val year: Int, val driveType: String, val enginePower: Int) {
    open fun displayInfo() {
        println("Model: $mark, Year: $year, Drive Type: $driveType, Engine Power: $enginePower HP")
    }
}

class Crossover(mark: String, year: Int, driveType: String, enginePower: Int, val cargoSpace: Int) : Car(mark, year, driveType, enginePower) {
    override fun displayInfo() {
        super.displayInfo()
        println("Cargo Space: $cargoSpace cubic feet")
    }
}

class Sedan(mark: String, year: Int, driveType: String, enginePower: Int, val trunkSize: Int) : Car(mark, year, driveType, enginePower) {
    override fun displayInfo() {
        super.displayInfo()
        println("Trunk Size: $trunkSize liters")
    }
}

class SUV(mark: String, year: Int, driveType: String, enginePower: Int, val groundClearance: Int) : Car(mark, year, driveType, enginePower) {
    override fun displayInfo() {
        super.displayInfo()
        println("Ground Clearance: $groundClearance mm")
    }
}


class Coupe(mark: String, year: Int, driveType: String, enginePower: Int, val doors: Int) : Car(mark, year, driveType, enginePower) {
    override fun displayInfo() {
        super.displayInfo()
        println("Doors: $doors")
    }
}

class CarBuilder {
    private var mark: String? = null
    private var year: Int? = null
    private var driveType: String? = null
    private var enginePower: Int? = null

    fun setMark(mark: String) = apply { this.mark = mark }
    fun setYear(year: Int) = apply { this.year = year }
    fun setDriveType(driveType: String) = apply { this.driveType = driveType }
    fun setEnginePower(enginePower: Int) = apply { this.enginePower = enginePower }

    fun build(): Car {
        return Car(mark!!, year!!, driveType!!, enginePower!!)
    }
}

fun createRandomCar(): Car {
    val marks = listOf("Mercedes", "BMW", "Audi", "Lada")
    val driveTypes = listOf("front", "rear", "all")
    val random = java.util.Random()

    return CarBuilder()
        .setMark(marks[random.nextInt(marks.size)])
        .setYear(2000 + random.nextInt(25))
        .setDriveType(driveTypes[random.nextInt(driveTypes.size)])
        .setEnginePower(100 + random.nextInt(201))
        .build()
}


fun race(cars: MutableList<Car>, raceResults: StringBuilder) {
    while (cars.size > 1) {
        val nextRoundCars = mutableListOf<Car>()
        cars.shuffle(Random)

        for (i in cars.indices step 2) {
            if (i + 1 < cars.size) {
                val car1 = cars[i]
                val car2 = cars[i + 1]
                val winner = if (car1.enginePower > car2.enginePower) car1 else car2
                raceResults.append("--- Гонка между ${car1.mark} и ${car2.mark}, Победитель ${winner.mark}\n")
                nextRoundCars.add(winner)
            } else {
                raceResults.append("--- ${cars[i].mark} - Нет пары, следующий круг\n")
                nextRoundCars.add(cars[i])
            }
        }
        cars.clear()
        cars.addAll(nextRoundCars)
    }

    if (cars.size == 1) {
        raceResults.append("Победитель ${cars[0].mark}\n")
    } else {
        raceResults.append("Нет победителя\n")
    }
}