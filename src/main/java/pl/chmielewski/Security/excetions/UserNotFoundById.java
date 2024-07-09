package pl.chmielewski.Security.excetions;

public class UserNotFoundById extends RuntimeException{
    public UserNotFoundById(Long id) {
        super("Nie znaleziono uzytkownika z id: " + id);
    }
}
