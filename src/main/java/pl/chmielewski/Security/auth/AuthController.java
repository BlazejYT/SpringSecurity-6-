package pl.chmielewski.Security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.chmielewski.Security.requests.LoginUserDto;
import pl.chmielewski.Security.requests.RegisterUserDTO;
import pl.chmielewski.Security.responses.UserLoginSuccessesDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserDTO registerUserDTO) {
        authService.register(registerUserDTO);
        return new ResponseEntity<>("Utworzono użytkownika", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginSuccessesDTO> login(@RequestBody LoginUserDto dto){
        UserLoginSuccessesDTO login = authService.login(dto);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }
}
