package rahma.backend.gestionPDEK.Configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import rahma.backend.gestionPDEK.Entity.User;

@Service
public class JWTService {
    private static final String SECRET_KEY = "SECRET123SECRET123SECRET123SECRET123"; // Doit faire au moins 32 caractères
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getNom());
        claims.put("permissions", user.getRole().getPermissions());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getMatricule()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration: 10 heures
                .signWith(key, SignatureAlgorithm.HS256) // Utilisation de la clé correctement formatée
                .compact();
    }
}
