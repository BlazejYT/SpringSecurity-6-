package pl.chmielewski.Security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.chmielewski.Security.excetions.UserNotFoundByEmail;
import pl.chmielewski.Security.excetions.UserNotFoundById;
import pl.chmielewski.Security.requests.RegisterUserDTO;
import pl.chmielewski.Security.token.TokenService;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public List<User> users() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundById(id));
        tokenService.deleteAllByUser(user);
        userRepository.delete(user);
    }

    public User createUser(RegisterUserDTO newUser) {
        User user = new User(
                newUser.firstname(),
                newUser.surname(),
                newUser.email()
        );
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(newUser.password()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundByEmail(email));
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
