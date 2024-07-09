package pl.chmielewski.Security.responses;

public record UserRegisterSucceed(
        String token,
        String email
) {
}
