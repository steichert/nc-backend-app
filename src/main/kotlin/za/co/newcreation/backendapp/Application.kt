package za.co.newcreation.backendapp

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableR2dbcAuditing
@EnableR2dbcRepositories
@EnableSchedulerLock(defaultLockAtMostFor = "PT5M")
@SpringBootApplication(scanBasePackages = ["za.co.newcreation"])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
