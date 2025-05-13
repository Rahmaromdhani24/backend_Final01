package rahma.backend.gestionPDEK.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activation de CORS ici
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/auth/getUser/**").hasAnyRole("ADMIN", "OPERATEUR", "CHEF_DE_LIGNE" , "AGENT_QUALITE" , "TECHNICIEN") 
                .requestMatchers("/pdek/**").hasAnyRole("ADMIN", "OPERATEUR", "CHEF_DE_LIGNE" , "AGENT_QUALITE" , "TECHNICIEN") 
                .requestMatchers("/operations/**").hasAnyRole("ADMIN", "OPERATEUR", "CHEF_DE_LIGNE" , "AGENT_QUALITE" , "TECHNICIEN") 
                .requestMatchers("/admin/**").hasAnyRole("ADMIN" ,"OPERATEUR" ,"CHEF_DE_LIGNE" ,"AGENT_QUALITE")
                .requestMatchers("/statistiques/**").hasAnyRole("SUPER_ADMIN" ,"ADMIN" ,"CHEF_DE_LIGNE" ,"AGENT_QUALITE" ,"TECHNICIEN")
                .requestMatchers("/operator/**").hasRole("OPERATEUR")
                .requestMatchers("/superAdmin/**").hasAnyRole("SUPER_ADMIN","ADMIN")
                .requestMatchers("/admin2/**").hasAnyRole("SUPER_ADMIN","ADMIN")
                .requestMatchers("/planAction/**").hasAnyRole("ADMIN", "OPERATEUR", "CHEF_DE_LIGNE" , "AGENT_QUALITE" , "TECHNICIEN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Frontend Angular
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
