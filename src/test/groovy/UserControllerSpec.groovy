import com.evaluacion2023.config.security.JwtToken
import com.evaluacion2023.controller.UsuarioController
import com.evaluacion2023.dto.PhoneDto
import com.evaluacion2023.dto.UserDto
import com.evaluacion2023.service.impl.LoginServiceImpl
import com.evaluacion2023.service.interfaces.UsuarioService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.MockMvc
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.http.HttpStatus.*
import spock.lang.Specification
import org.springframework.http.MediaType
import org.springframework.http.HttpStatus;




class UserControllerSpec extends Specification {
    def usuarioService = Mock(UsuarioService)
    def loginService = Mock(LoginServiceImpl)
    def jwtToken = Mock(JwtToken)
    def usuarioController = new UsuarioController(usuarioService, loginService, jwtToken)
    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc = standaloneSetup(usuarioController).build()


    def "Test signUp method"() {
        given:
        UserDto userDto = new UserDto(
                name:"balto",
                email: "test@example.com",
                password: "Cata1c90",
                phones: [
                        new PhoneDto(number: 123456789, cityCode: 123, countryCode: "USD"),
                        new PhoneDto(number: 987654321, cityCode: 321, countryCode: "CL")
                ]
        )

        when:
        def response = mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andReturn()
                .response


        then:
        //response.status == HttpStatus.BAD_REQUEST.value()
        response.status == HttpStatus.CREATED.value()
    }

    def "Test login method"() {
        given:
        String validBearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkNWVjZmUzNy1mMjVhLTRhYTUtOTIyNC1mZjlhNjc3MWJkMDUiLCJuYW1lIjoiYmFsdG8iLCJlbWFpbCI6ImNvcnJlbzNAY29ycmVvMy5jbCIsImlhdCI6MTY4OTY5MzgxOSwiZXhwIjoxNjg5NzAxMDE5fQ.CxydctZrHqFVk0p-ev1lvbi-TsM594e1Ln5esomdWEc"

        when:
        def response = mockMvc.perform(get("/login")
                .header("Authorization", validBearerToken))
                .andReturn()
                .response

        then:
        // Aquí agregamos la aserción para verificar que el código de estado es 202 (HttpStatus.ACCEPTED.value())
        response.status == HttpStatus.ACCEPTED.value()
    }
}
