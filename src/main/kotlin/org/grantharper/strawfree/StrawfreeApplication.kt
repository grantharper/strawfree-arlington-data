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


    override fun run(vararg args: String?) {
        logger.info("Kotlin command line running")
        val inputFile = FileReader("input.csv")
        val records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(inputFile)
        for (record in records) {
            val name = record.get("Name")
            val starCount = record.get("Star Count")
            logger.info("name=${name}, starCount=${starCount}")
        }

    }
}

fun main(args: Array<String>) {
    runApplication<StrawfreeApplication>(*args)
}




