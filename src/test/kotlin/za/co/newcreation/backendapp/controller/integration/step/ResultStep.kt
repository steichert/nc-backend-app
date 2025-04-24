package za.co.newcreation.backendapp.controller.integration.step

import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import za.co.newcreation.backendapp.controller.integration.IntegrationTest
import za.co.newcreation.backendapp.controller.integration.common.ScenarioState

@Component
class ResultStep(
    @Autowired private val scenarioState: ScenarioState,
    @Autowired private val mockMvc: MockMvc
): IntegrationTest(mockMvc) {

    fun thenResponseContentIsNotNull() {
        Assertions.assertThat(scenarioState.resultMap[scenarioState.requestId]?.response?.contentAsString).isNotNull()
    }

    fun thenResponseStatusIs(expectedStatus: Int) {
        Assertions.assertThat(scenarioState.resultMap[scenarioState.requestId]?.response?.status).isNotNull()
        Assertions.assertThat(scenarioState.resultMap[scenarioState.requestId]?.response?.status).isEqualTo(expectedStatus)
    }
}