package org.geoandri.quarztest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QuarztestApplication

fun main(args: Array<String>) {
	runApplication<QuarztestApplication>(*args)
}
