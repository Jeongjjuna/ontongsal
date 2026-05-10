package yjh.ontongsal.testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SpringTestingApplication

fun main(args: Array<String>) {
    runApplication<SpringTestingApplication>(*args)
}
