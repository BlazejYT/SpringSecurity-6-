package pl.chmielewski.Security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.chmielewski.Security.user.Role;

@Configuration
@EnableWebSecurity
public class WebConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationFilter authenticationFilter;

    @Autowired
    public WebConfig(UserDetailsService userDetailsService, AuthenticationProvider authenticationProvider, AuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.authenticationFilter = authenticationFilter;
    }

    private final String[] USER_URL_LIST = {
            "/users/all",
    };

    private final String[] WHITE_URL_LIST = {
            "/auth/**",
            "/h2-console/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_URL_LIST).permitAll()
                        .requestMatchers(USER_URL_LIST).hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                        .requestMatchers("/**").hasAnyAuthority(Role.ADMIN.name())
                        .anyRequest()
                        .authenticated()
                )
                .userDetailsService(userDetailsService)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
                return http.build();
    }
}
