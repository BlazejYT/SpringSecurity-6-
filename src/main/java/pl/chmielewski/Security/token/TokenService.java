package pl.chmielewski.Security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.chmielewski.Security.user.User;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(String tokenJwt, User user){
        Token token = new Token();
        token.setToken(tokenJwt);
        token.setExpired(false);
        token.setRevoked(false);
        token.setUser(user);
        return tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user){
        Optional<List<Token>> allByUser = tokenRepository.findAllByUser(user);

        allByUser.ifPresent(tokens -> {
            for (Token token: tokens){
                token.setRevoked(true);
                tokenRepository.save(token);
            }
        });
    }


    public void deleteAllByUser(User user) {
        Optional<List<Token>> allByUser = tokenRepository.findAllByUser(user);
        if(allByUser.isPresent()){
            List<Token> tokens = allByUser.get();
            tokenRepository.deleteAll(tokens);
        }
    }
}
