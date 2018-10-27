package org.grantharper.strawfree

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.apache.commons.csv.CSVFormat
import java.io.FileReader


@SpringBootApplication
class StrawfreeApplication: CommandLineRunner {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    val inputFilename = "input.csv"
    val nameHeader = "Name"
    val starCountHeader = "Star Count"

    override fun run(vararg args: String?) {
        logger.info("Kotlin command line running")
        processCsv()

    }

    private fun processCsv() {

        val inputFile = FileReader(inputFilename)
        val records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(inputFile)
        for (record in records) {
            val name = record.get(nameHeader)
            val starCount = record.get(starCountHeader)
            logger.info("name=${name}, starCount=${starCount}")
        }
    }

}

fun main(args: Array<String>) {
    runApplication<StrawfreeApplication>(*args)
}




