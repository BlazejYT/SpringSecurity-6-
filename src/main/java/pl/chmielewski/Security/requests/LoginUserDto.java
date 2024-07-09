package pl.chmielewski.Security.requests;

public record LoginUserDto(
        String email,
        String password
) {
}
