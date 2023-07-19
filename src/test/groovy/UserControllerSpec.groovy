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

    //The magic happens here
    MockMvc mockMvc = standaloneSetup(usuarioController).build()


    def "Test signUp method"() {
        given:
        UserDto userDto = new UserDto(
                name:"balto",
                email: "test@example.com",
                password: "password",
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
        response.status == HttpStatus.BAD_REQUEST.value()
    }
}
