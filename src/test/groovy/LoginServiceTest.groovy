import com.evaluacion2023.config.exceptions.TokenErrorException
import com.evaluacion2023.config.security.JwtToken
import com.evaluacion2023.repository.UserRepository
import com.evaluacion2023.service.impl.LoginServiceImpl
import com.evaluacion2023.service.interfaces.LoginService
import spock.lang.Specification

class LoginServiceTest extends Specification {
    JwtToken jwtToken
    LoginService loginService
    UserRepository userRepository

    def setup() {
        userRepository = Mock(UserRepository)
        jwtToken = Mock(JwtToken)
        loginService = new LoginServiceImpl(userRepository, jwtToken)
    }

    def "Test loginUser method with invalid token"() {
        given:
        String invalidBearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjA1NDZiZC02MjE3LTQ3OTQtOWFmYS1kNWE1NzkyZGVjZWUiLCJuYW1lIjoiYmFsdG8iLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJpYXQiOjE2ODk3OTQ5NTQsImV4cCI6MTY4OTgwMjE1NH0.BiNMn3zTTy_3yWYxcigdQF9Tft7vvygBwRZVZi28GF4"

        loginService.getUserIdFromToken(_) >> {
            throw new TokenErrorException("El token anterior es inválido")
        }

        when:
        def response = loginService.loginUser(invalidBearerToken)

        then:
        thrown(TokenErrorException)
    }
}
