package pl.chmielewski.Security.requests;

public record RegisterUserDTO(
        String firstname,
        String surname,
        String email,
        String password
) {
}
