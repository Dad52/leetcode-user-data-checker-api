package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

@SpringBootTest
class DemoApplicationTests {

    @Test
    fun contextLoads() {
        val first = mutableListOf<Long>()
        for (i in 0 until 10) {
            val measuredTime = measureNanoTime {
                MyEnum.values().find { it.name == "FOURTH" }
            }
            first.add(measuredTime)
        }

        println("first middle time = ${first.sum() / first.size}}")

        first.clear()
        val values = MyEnum.values()
        for (i in 0 until 10) {
            val measuredTime = measureNanoTime {
                values.find { it.name == "FOURTH" }
            }
            first.add(measuredTime)
        }

        println("second middle time = ${first.sum() / first.size}}")

        first.clear()
        val entries = MyEnum.entries
        for (i in 0 until 10) {
            val measuredTime = measureNanoTime {
                entries.find { it.name == "FOURTH" }
            }
            first.add(measuredTime)
        }

        println("third middle time = ${first.sum() / first.size}}")
    }

    private enum class MyEnum {
        FIRST, SECOND, THIRD, FOURTH, FIFTH
    }
}
