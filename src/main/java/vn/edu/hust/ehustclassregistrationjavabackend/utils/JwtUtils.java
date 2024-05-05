package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtils {
    public static final String JWT_SECRET = "abcdef";
    public static final long ACCESS_TOKEN_EXPIRED = 60L * 60 * 1000; // 1 hour
    public static final long REFRESH_TOKEN_EXPIRED = 30 * 24 * ACCESS_TOKEN_EXPIRED; // 1 month
    SecretKey key;

    JwtUtils() {
        byte[] keyByte = Base64.getDecoder().decode(JWT_SECRET.getBytes());
        this.key = new SecretKeySpec(keyByte, "SHA512");
    }

    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .id(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails, HashMap<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .id(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED))
                .signWith(key)
                .compact();

    }

    public String extractId(String token) {
        return extractClaims(token, Claims::getId);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        if(extractId(token).equals(userDetails.getUsername())){
            return !isTokenExpired(token);
        }
        return false;
    }
    public boolean isTokenExpired(String token){
        Date date = extractClaims(token,Claims::getExpiration);
        return date.before(new Date(System.currentTimeMillis()));
    }

    <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }
}
