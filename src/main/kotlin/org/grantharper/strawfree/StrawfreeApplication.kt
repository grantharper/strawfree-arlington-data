package org.grantharper.strawfree

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StrawfreeApplication: CommandLineRunner {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)


    override fun run(vararg args: String?) {
        logger.info("Kotlin command line running")

    }
}

fun main(args: Array<String>) {
    runApplication<StrawfreeApplication>(*args)
}




