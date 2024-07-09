package pl.chmielewski.Security.excetions;

public class UserExistsByEmail extends RuntimeException{

    public UserExistsByEmail(String email) {
        super("Użytkownik istnieje o podanym email'u: " + email);
    }
}
