package com.example.demo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.util.JSONPObject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.relational.core.sql.In
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.create()
    }
}

data class ExampleJson2KtKotlin(

    @JsonProperty("data") var data: Data? = Data(),

    )

data class AllQuestionsCount(

    @JsonProperty("difficulty") var difficulty: String? = null,
    @JsonProperty("count") var count: Int? = null,

    )

data class Contributions(

    @JsonProperty("points") var points: Int? = null,

    )

data class Profile(

    @JsonProperty("reputation") var reputation: Int? = null,
    @JsonProperty("ranking") var ranking: Int? = null,

    )

data class AcSubmissionNum(

    @JsonProperty("difficulty") var difficulty: String? = null,
    @JsonProperty("count") var count: Int? = null,
    @JsonProperty("submissions") var submissions: Int? = null,

    )

data class TotalSubmissionNum(

    @JsonProperty("difficulty") var difficulty: String? = null,
    @JsonProperty("count") var count: Int? = null,
    @JsonProperty("submissions") var submissions: Int? = null,

    )

data class SubmitStats(

    @JsonProperty("acSubmissionNum") var acSubmissionNum: ArrayList<AcSubmissionNum> = arrayListOf(),
    @JsonProperty("totalSubmissionNum") var totalSubmissionNum: ArrayList<TotalSubmissionNum> = arrayListOf(),

    )

data class MatchedUser(

    @JsonProperty("contributions") var contributions: Contributions? = Contributions(),
    @JsonProperty("profile") var profile: Profile? = Profile(),
    @JsonProperty("submissionCalendar") var submissionCalendar: String? = null,
    @JsonProperty("submitStats") var submitStats: SubmitStats? = SubmitStats(),

    )

data class Data(

    @JsonProperty("allQuestionsCount") var allQuestionsCount: ArrayList<AllQuestionsCount> = arrayListOf(),
    @JsonProperty("matchedUser") var matchedUser: MatchedUser? = MatchedUser(),

    )

@Service
class ApiService() {

    fun makePostRequest(url: String, body: Any?): ExampleJson2KtKotlin? {
        val restTemplate = RestTemplate()
        val headers = org.springframework.http.HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Content-Type", "application/json")
            set("referer", "https://leetcode.com/dad52/")
        }

        val request = HttpEntity(body, headers)
        return restTemplate.postForObject(url, request, ExampleJson2KtKotlin::class.java)
    }
}

class StatsResponse(
    val status: String,
    val message: String,
    val totalSolved: Int,
    val totalQuestions: Int,
    val easySolved: Int,
    val totalEasy: Int,
    val mediumSolved: Int,
    val totalMedium: Int,
    val hardSolved: Int,
    val totalHard: Int,
    val acceptanceRate: Float,
    val ranking: Int,
    val contributionPoints: Int,
    val reputation: Int,
    val submissionCalendar: Map<String, Int>,
) {

    companion object {
        fun error(status: String, message: String): StatsResponse {
            return StatsResponse(status, message, 0, 0, 0, 0, 0, 0, 0, 0, 0f, 0, 0, 0, emptyMap())
        }
    }
}

// @Service
// class StatsServiceImpl {
//     fun getStats(username: String?): StatsResponse {
//         val client: OkHttpClient = OkHttpClient().newBuilder()
//             .build()
//         val mediaType: MediaType = MediaType.parse("application/json")
//         val query = String.format(
//             "{\"query\":\"query getUserProfile(\$username: String!) { allQuestionsCount { difficulty count } matchedUser(username: \$username) { contributions { points } profile { reputation ranking } submissionCalendar submitStats { acSubmissionNum { difficulty count submissions } totalSubmissionNum { difficulty count submissions } } } } \",\"variables\":{\"username\":\"%s\"}}",
//             username
//         )
//         val body: RequestBody = RequestBody.create(mediaType, query)
//         val request: Request = Builder()
//             .url("https://leetcode.com/graphql/")
//             .method("POST", body)
//             .addHeader("referer", String.format("https://leetcode.com/%s/", username))
//             .addHeader("Content-Type", "application/json")
//             .build()
//
//         try {
//             val response: Response = client.newCall(request).execute()
//
//             // Inspect response
//             val responseString: String = response.body().string()
//             val jsonObject = JSONObject(responseString)
//
//             return if (response.isSuccessful()) {
//                 // Parse GraphQL response
//
//                 // User not found
//
//                 if (jsonObject.has("errors")) {
//                     StatsResponse.error("error", "user does not exist")
//                 } else { // Parse user info
//                     decodeGraphqlJson(jsonObject)
//                 }
//             } else {
//                 StatsResponse.error("error", jsonObject.getString("error"))
//             }
//         } catch (ex: IOException) {
//             return StatsResponse.error("error", ex.message)
//         } catch (ex: JSONException) {
//             return StatsResponse.error("error", ex.message)
//         }
//     }
//
//     private fun decodeGraphqlJson(json: JSONObject): StatsResponse {
//         var totalSolved = 0
//         var totalQuestions = 0
//         var easySolved = 0
//         var totalEasy = 0
//         var mediumSolved = 0
//         var totalMedium = 0
//         var hardSolved = 0
//         var totalHard = 0
//         var acceptanceRate = 0f
//         var ranking = 0
//         var contributionPoints = 0
//         var reputation = 0
//
//         val submissionCalendar: MutableMap<String, Int> = TreeMap()
//
//         try {
//             val data = json.getJSONObject("data")
//             val allQuestions = data.getJSONArray("allQuestionsCount")
//             val matchedUser = data.getJSONObject("matchedUser")
//             val submitStats = matchedUser.getJSONObject("submitStats")
//             val actualSubmissions = submitStats.getJSONArray("acSubmissionNum")
//             val totalSubmissions = submitStats.getJSONArray("totalSubmissionNum")
//
//             // Fill in total counts
//             totalQuestions = allQuestions.getJSONObject(0).getInt("count")
//             totalEasy = allQuestions.getJSONObject(1).getInt("count")
//             totalMedium = allQuestions.getJSONObject(2).getInt("count")
//             totalHard = allQuestions.getJSONObject(3).getInt("count")
//
//             // Fill in solved counts
//             totalSolved = actualSubmissions.getJSONObject(0).getInt("count")
//             easySolved = actualSubmissions.getJSONObject(1).getInt("count")
//             mediumSolved = actualSubmissions.getJSONObject(2).getInt("count")
//             hardSolved = actualSubmissions.getJSONObject(3).getInt("count")
//
//             // Fill in etc
//             val totalAcceptCount = actualSubmissions.getJSONObject(0).getInt("submissions").toFloat()
//             val totalSubCount = totalSubmissions.getJSONObject(0).getInt("submissions").toFloat()
//             if (totalSubCount != 0f) {
//                 acceptanceRate = round((totalAcceptCount / totalSubCount) * 100, 2)
//             }
//
//             contributionPoints = matchedUser.getJSONObject("contributions").getInt("points")
//             reputation = matchedUser.getJSONObject("profile").getInt("reputation")
//             ranking = matchedUser.getJSONObject("profile").getInt("ranking")
//
//             val submissionCalendarJson = JSONObject(matchedUser.getString("submissionCalendar"))
//
//             for (timeKey in submissionCalendarJson.keySet()) {
//                 submissionCalendar[timeKey] = submissionCalendarJson.getInt(timeKey)
//             }
//         } catch (ex: JSONException) {
//             return StatsResponse.error("error", ex.message)
//         }
//
//         return StatsResponse(
//             "success",
//             "retrieved",
//             totalSolved,
//             totalQuestions,
//             easySolved,
//             totalEasy,
//             mediumSolved,
//             totalMedium,
//             hardSolved,
//             totalHard,
//             acceptanceRate,
//             ranking,
//             contributionPoints,
//             reputation,
//             submissionCalendar
//         )
//     }
//
//     private fun round(d: Float, decimalPlace: Int): Float {
//         var bd = BigDecimal(d.toString())
//         bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP)
//         return bd.toFloat()
//     }
// }

// kotlin
// import khttp.post as httpPost
// import org.json.JSONObject
//
// object LeetcodeApi {
//
//     private const val URL = "ВАША_URL_ССЫЛКА"
//     private const val QUERY = "ВАША_ЗАПРОС_QUERY"
//     private const val QUERY2 = "ВАША_ЗАПРОС_QUERY2"
//
//     fun getLeetCodeUserData(username: String?): JSONObject? {
//         if (!username.isNullOrEmpty()) {
//             val body = JSONObject(mapOf(
//                 "query" to QUERY,
//                 "variables" to mapOf("username" to username)
//             ))
//
//             val response = httpPost(
//                 url = URL,
//                 json = body,
//                 headers = mapOf("Content-Type" to "application/json")
//             )
//
//             val data = response.jsonObject
//             return if (!data.has("errors")) data
//             else JSONObject(mapOf("message" to data.getJSONArray("errors").getJSONObject(0).getString("message")))
//         }
//
//         return JSONObject(mapOf("message" to "username invalid"))
//     }
//
//     fun getLeetCodeUserContestDetails(username: String?): JSONObject? {
//         if (!username.isNullOrEmpty()) {
//             val body = JSONObject(mapOf(
//                 "query" to QUERY2,
//                 "variables" to mapOf("username" to username)
//             ))
//
//             val response = httpPost(
//                 url = URL,
//                 json = body,
//                 headers = mapOf("Content-Type" to "application/json")
//             )
//
//             val data = response.jsonObject
//
//             return if (!data.has("errors")) {
//                 val ranks = data.getJSONObject("data").getJSONObject("userContestRanking")
//                 val history = data.getJSONObject("data").getJSONArray("userContestRankingHistory")
//                 val filtered = history.filter { it as JSONObject; it.getBoolean("attended") }
//
//                 JSONObject(mapOf(
//                     "userContestRanking" to ranks,
//                     "userContestRankingHistory" to filtered
//                 ))
//             } else JSONObject(mapOf("message" to data.getJSONArray("errors").getJSONObject(0).getString("message")))
//         }
//
//         return JSONObject(mapOf("message" to "username invalid"))
//     }
// }