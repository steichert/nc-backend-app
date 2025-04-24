package za.co.newcreation.backendapp.controller.integration

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch

abstract class IntegrationTest(
    private val mockMvc: MockMvc
) {
    companion object {
        const val EVENT_BASE_URL = "/event"
    }

    fun postRequest(path: String, body: String): MvcResult {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andReturn()

        return mockMvc.perform(asyncDispatch(result)).andReturn()
    }

    fun getRequest(path: String): MvcResult {
        val result = mockMvc.perform(MockMvcRequestBuilders.get(path).contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        return mockMvc.perform(asyncDispatch(result)).andReturn()
    }
}