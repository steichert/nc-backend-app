package za.co.newcreation.backendapp.controller.integration.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import za.co.newcreation.backendapp.controller.integration.common.ScenarioState

@Configuration
class IntegrationTestConfiguration {
    @Bean
    fun scenarioState(): ScenarioState =
        ScenarioState()
}