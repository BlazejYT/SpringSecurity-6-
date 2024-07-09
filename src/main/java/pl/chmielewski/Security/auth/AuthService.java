package pl.chmielewski.Security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.chmielewski.Security.excetions.UserExistsByEmail;
import pl.chmielewski.Security.requests.LoginUserDto;
import pl.chmielewski.Security.requests.RegisterUserDTO;
import pl.chmielewski.Security.responses.UserLoginSuccessesDTO;
import pl.chmielewski.Security.responses.UserRegisterSucceed;
import pl.chmielewski.Security.token.JwtService;
import pl.chmielewski.Security.token.TokenService;
import pl.chmielewski.Security.user.User;
import pl.chmielewski.Security.user.UserService;

@Service
public class AuthService {

    private int exp = 7 * 24 * 60 * 60 * 1000;
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserService userService, TokenService tokenService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public UserLoginSuccessesDTO login(LoginUserDto dto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                ));
        User user = userService.getUserByEmail(dto.email());
        tokenService.revokeAllUserTokens(user);

        String token = jwtService.generateToken(user.getUsername(), exp);
        tokenService.createToken(token, user);

        return new UserLoginSuccessesDTO(user.getEmail(), user.getRole().name(), token);
    }


    public UserRegisterSucceed register(RegisterUserDTO newUser) {
        if (userService.userExistsByEmail(newUser.email())) {
            throw new UserExistsByEmail(newUser.email());
        }
        User user = userService.createUser(newUser);
        String token = jwtService.generateToken(user.getEmail(), exp);
        tokenService.createToken(token, user);
        return new UserRegisterSucceed(token, user.getEmail());
    }
}
