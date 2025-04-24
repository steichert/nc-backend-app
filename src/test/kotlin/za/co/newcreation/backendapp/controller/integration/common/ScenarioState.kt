package za.co.newcreation.backendapp.controller.integration.common

import org.springframework.test.web.servlet.MvcResult

class ScenarioState {
    var requestId: String = ""
    var resultMap: MutableMap<String, MvcResult> = mutableMapOf()
}