package org.grantharper.strawfree

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.csv.CSVFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.FileReader
import java.io.FileWriter


@SpringBootApplication
class StrawfreeApplication: CommandLineRunner {

    val logger: Logger = LoggerFactory.getLogger(javaClass)
    val inputFilename = "input.csv"
    val nameHeader = "Name"
    val starCountHeader = "Star Count"
    val addressHeader = "Address"

    override fun run(vararg args: String?) {
        logger.info("Kotlin app running")
        writeJson(processCsv())
    }

    fun processCsv(): List<StrawfreeInput> {

        val inputFile = FileReader(inputFilename)
        val records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(inputFile)
        return records.map { csvRecord -> StrawfreeInput(csvRecord.get(nameHeader).trim(),
                csvRecord.get(starCountHeader).trim().toInt(),
                csvRecord.get(addressHeader).trim()) }
                .toList()
    }

    fun writeJson(strawfreeInputList: List<StrawfreeInput>) {
        logger.info(strawfreeInputList.toString())
        val outputFilename = "output.json"
        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(strawfreeInputList)
        logger.info(json)
        val outputFile = FileWriter(outputFilename)
        outputFile.write(json)
        outputFile.close()
    }

    fun callMapsAPI(address: String): Pair<Double, Double> {

        return Pair(0.0, 0.0)
    }

}

data class StrawfreeInput(val name: String, val starCount: Int, val address: String)

fun main(args: Array<String>) {
    runApplication<StrawfreeApplication>(*args)
}




