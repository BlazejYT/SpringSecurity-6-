package pl.chmielewski.Security.excetions;

public class UserNotFoundByEmail extends RuntimeException{

    public UserNotFoundByEmail(String email) {
        super("Nie znaleziono użytkownika z email'em: " + email);
    }
}
