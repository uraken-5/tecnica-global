import com.evaluacion2023.config.security.JwtToken
import com.evaluacion2023.controller.UsuarioController
import com.evaluacion2023.dto.UserDto
import com.evaluacion2023.service.interfaces.LoginService
import com.evaluacion2023.service.interfaces.UsuarioService
import org.springframework.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import org.springframework.http.ResponseEntity
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.ACCEPTED

class UsuarioControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    UsuarioService usuarioService = Mock(UsuarioService)
    @Shared
    @AutoCleanup
    LoginService loginService = Mock(LoginService)
    @Shared
    @AutoCleanup
    JwtToken jwtToken = Mock(JwtToken)

    @Shared
    ResponseEntity<Map<String, Object>> signUpResponse
    @Shared
    ResponseEntity<Map<String, Object>> loginResponse

    def setup() {
        usuarioService.saveUser(_) >> [
                id: "123456",
                created: "Nov 16, 2021 12:51:43 PM",
                lastLogin: "Nov 16, 2021 12:51:43 PM",
                token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxpb0B0ZXN0...",
                isActive: true,
                name: "John Doe",
                email: "johndoe@example.com"
        ]
        loginService.loginUser(_) >> [
                id: "123456",
                created: "Nov 16, 2021 12:51:43 PM",
                lastLogin: "Nov 16, 2021 12:51:43 PM",
                token: "newToken",
                isActive: true,
                name: "John Doe",
                email: "johndoe@example.com"
        ]
        jwtToken.extractTokenFromAuthorizationHeader(_) >> "token123"
    }

    def "signUp should return CREATED response with user data"() {
        given:
        def userDTO = new UserDto()

        when:
        signUpResponse = new UsuarioController(usuarioService, loginService, jwtToken).signUp(userDTO)

        then:
        signUpResponse.statusCode == CREATED
        signUpResponse.body.id == "123456"
        signUpResponse.body.name == "John Doe"
        // Assert other properties
    }

    def "login should return ACCEPTED response with user data"() {
        given:
        def authorizationHeader = "Bearer token123"

        when:
        loginResponse = new UsuarioController(usuarioService, loginService, jwtToken).login(authorizationHeader)

        then:
        loginResponse.statusCode == ACCEPTED
        loginResponse.body.id == "123456"
        loginResponse.body.name == "John Doe"
        // Assert other properties
    }
}

