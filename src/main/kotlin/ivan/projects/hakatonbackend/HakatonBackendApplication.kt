package ivan.projects.hakatonbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HakatonBackendApplication

fun main(args: Array<String>) {
	runApplication<HakatonBackendApplication>(*args)
}
