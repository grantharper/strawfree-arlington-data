package org.grantharper.strawfree

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import org.apache.commons.csv.CSVFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.io.FileReader
import java.io.FileWriter


@SpringBootApplication
class StrawfreeApplication : CommandLineRunner {

    val logger: Logger = LoggerFactory.getLogger(StrawfreeApplication::class.java)
    val inputCsv = "input.csv"
    val inputJson = "input.json"
    val outputJson = "output.json"
    val nameHeader = "Name"
    val starCountHeader = "starCount"
    val addressHeader = "Address"
    val apiClient = ApiClient()
    val objectMapper = ObjectMapper()

    override fun run(vararg args: String?) {
        logger.info("Kotlin app running")
        writeInputJson(processCsv())
        val inputData = readInputJson()
        val outputData: MutableList<StrawfreeOutput> = mutableListOf()
        for (business in inputData) {
            val apiCoordinates = callMapsAPI(business.address)
            if (apiCoordinates == null) {
                logger.error("Null coordinates received for business name=${business.name}")
            }
            val latLng = LatLng(apiCoordinates.first, apiCoordinates.second)
            outputData.add(StrawfreeOutput(business.name,business.starCount, business.address, latLng))
        }
        writeOutputJson(outputData)

    }

    fun processCsv(): List<StrawfreeInput> {

        val inputFile = FileReader(inputCsv)
        val records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(inputFile)
        return records.map { csvRecord ->
            StrawfreeInput(csvRecord.get(nameHeader).trim(),
                    csvRecord.get(starCountHeader).trim().toInt(),
                    csvRecord.get(addressHeader).trim())
        }
                .toList()
    }

    fun writeInputJson(strawfreeInputList: List<StrawfreeInput>) {
        logger.debug(strawfreeInputList.toString())
        writeJson(strawfreeInputList, inputJson)

    }

    fun writeOutputJson(strawfreeOutputList: List<StrawfreeOutput>) {
        writeJson(strawfreeOutputList, outputJson)
    }

    fun readInputJson(): List<StrawfreeInput> {
        return readJson(inputJson)
    }

    fun readJson(filename: String): List<StrawfreeInput> {
        val inputFile = FileReader(filename)
        val contents = inputFile.readText()
        val javaType: CollectionType = objectMapper.typeFactory.constructCollectionType(List::class.java, StrawfreeInput::class.java)
        val strawfreeInputList: List<StrawfreeInput> = objectMapper.readValue(contents, javaType)
        return strawfreeInputList
    }

    fun writeJson(data: Any, filename: String) {

        val json = objectMapper.writeValueAsString(data)
        logger.info(json)
        val outputFile = FileWriter(filename)
        outputFile.write(json)
        outputFile.close()
    }

    fun callMapsAPI(address: String): Pair<Double?, Double?> {

        return apiClient.callApi(address)
    }

}

class StrawfreeInput(var name: String, var starCount: Int, var address: String){
    constructor(): this("", -1, "") //ugly, but necessary for Jackson to work (no arg constructor)
}

data class StrawfreeOutput(val name: String, val starCount: Int, val address: String, val coordinates: LatLng)

data class LatLng(val lat: Double?, val lng: Double?)

class ApiClient() {

    val logger: Logger = LoggerFactory.getLogger(ApiClient::class.java)
    val objectMapper = ObjectMapper()
    val webClient = WebClient.create("http://localhost:8080")

    fun callApi(address: String): Pair<Double?, Double?> {
        val response = webClient.post().uri("/address").contentType(MediaType.APPLICATION_JSON)
                .syncBody(surroundWithQuotes(address)).retrieve().bodyToMono(List::class.java).block()
        val result = response!!.get(0) as LinkedHashMap<String, Double>
        logger.debug("response=" + response)
        val pair = Pair(result["lat"], result["lng"])
        logger.debug("pair=" + pair)
        return pair
    }

    private fun surroundWithQuotes(input: String): String {
        return "\"$input\""
    }
}


fun main(args: Array<String>) {
    runApplication<StrawfreeApplication>(*args)
}




