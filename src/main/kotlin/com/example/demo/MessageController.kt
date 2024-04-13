package com.example.demo

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.format.DateTimeFormatter

@RestController
class MessageController(
    private val apiService: ApiService,
) {
    @GetMapping("/api")
    fun index(
        @RequestParam("name") name: String,
    ): String {
        return "Hello, $name!"
    }

    @PostMapping("/test")
    fun getTest(
        @RequestBody product: Product?,
    ): String {
        val mutableListOf = mutableListOf<String>()
        val apiUrl = "https://leetcode.com/graphql/"
        val query = String.format(
            "{\"query\":\"query getUserProfile(\$username: String!) { allQuestionsCount { difficulty count } matchedUser(username: \$username) { contributions { points } profile { reputation ranking } submissionCalendar submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } } } \",\"variables\":{\"username\":\"%s\"}}",
            "Dad52"
        )

        val sb = mutableListOf

        println(query)
        try {
            val response = apiService.makePostRequest(apiUrl, query)
            val keys = extractKeysUsingRegex(response?.data?.matchedUser?.submissionCalendar ?: "")
            keys.forEach {
                val time = getRightTime(it.toLong())
                sb.add(time)
            }
            println("RESPONSE: $response")
        } catch (e: Exception) {
            println(e.toString())
        }
        sb.sort()
        return sb.joinToString()
    }

    private fun getRightTime(l: Long): String {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(l))
    }

    fun extractKeysUsingRegex(jsonString: String): List<String> {
        val regex = "\"(\\d+)\"".toRegex()
        return regex.findAll(jsonString).map { it.groupValues[1] }.toList()
    }

    private fun extracted(list: MutableList<String>) {
        val first = 1
        val second = 10000
        for (i in first..second) {
            val text =
                "Пример текстового содержимого, которое будет повторяться много раз. Такой текст отлично поддается сжатию GZIP из-за большого количества повторений и общих последовательностей."
            list.add(text)
        }
    }
}

data class Product(
    @field:JsonProperty("egg")
    val egg: String,
)