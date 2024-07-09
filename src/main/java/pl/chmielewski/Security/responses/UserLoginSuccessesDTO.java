package pl.chmielewski.Security.responses;

public record UserLoginSuccessesDTO(
        String email,
        String role,
        String token
) {
}
