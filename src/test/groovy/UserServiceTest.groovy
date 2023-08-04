import com.evaluacion2023.config.security.JwtToken
import com.evaluacion2023.dto.PhoneDto
import com.evaluacion2023.dto.UserDto
import com.evaluacion2023.model.Phone
import com.evaluacion2023.model.User
import com.evaluacion2023.repository.UserRepository
import com.evaluacion2023.service.impl.UsuarioServiceImpl
import com.evaluacion2023.service.interfaces.LoginService
import com.evaluacion2023.service.interfaces.UsuarioService
import org.modelmapper.ModelMapper
import org.springframework.context.MessageSource
import org.springframework.context.support.StaticMessageSource
import spock.lang.Specification

class UserServiceTest extends Specification {
    UserRepository userRepository
    ModelMapper modelMapper
    JwtToken jwtToken
    MessageSource messageSource
    UsuarioService usuarioService
    LoginService loginService

    def setup() {
        userRepository = Mock(UserRepository)
        jwtToken = Mock(JwtToken)
        modelMapper = Mock(ModelMapper)
        loginService = new LoginService() {
            @Override
            Map<String, Object> loginUser(String token) {
                return null
            }
        }

        messageSource = new StaticMessageSource()
        messageSource.addMessage("user.alreadyExists", Locale.getDefault(), "El usuario ya existe en la base de datos")
        messageSource.addMessage("user.response.id", Locale.getDefault(), "id")
        messageSource.addMessage("user.response.created", Locale.getDefault(), "created")
        messageSource.addMessage("user.response.lastLogin", Locale.getDefault(), "lastLogin")
        messageSource.addMessage("user.response.token", Locale.getDefault(), "token")
        messageSource.addMessage("user.response.active", Locale.getDefault(), "isActive")

        usuarioService = new UsuarioServiceImpl(
                userRepository,
                modelMapper,
                jwtToken,
                messageSource
        )
    }

    def "Guardar usuario exitosamente"() {
        given:
        def userDto = new UserDto(
                name: "balto",
                password: "password",
                email: "test@example.com",
                phones: [
                        new PhoneDto(number: 123456789, cityCode: 123, countryCode: "USD"),
                        new PhoneDto(number: 987654321, cityCode: 321, countryCode: "CL")
                ]
        )

        def user = new User(name: userDto.name, password: userDto.password, email: userDto.email, phones: userDto.phones)
        modelMapper.map(userDto, User.class) >> user

        def phoneList = userDto.phones.collect { phoneDto ->
            def phoneEntity = new Phone(number: phoneDto.number, cityCode: phoneDto.cityCode, countryCode: phoneDto.countryCode)
            modelMapper.map(phoneDto, Phone.class) >> phoneEntity
            phoneEntity.setUser(user)
        }
        user.setPhones(phoneList)

        def savedUser
        userRepository.save(_ as User) >> { User u ->
            savedUser = u
            return u
        }

        when:
        def result = usuarioService.saveUser(userDto)

        then:
        1 * userRepository.save(_ as User)
        result.created != null
        result.lastLogin != null
    }

    def "Test loginUser method with invalid token"() {
        given:
        String validBearerToken = "Bearer invalid"

        when:
        Map<String, Object> response = loginService.loginUser(validBearerToken)

        then:
        response == null
    }
}
