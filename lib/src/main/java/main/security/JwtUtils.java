package main.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.dbmodels.DbUser;

@Component
public class JwtUtils {

	private static String SECRET;
	@Value("${TIME_TO_LIVE}")
	private static long TIME_TO_LIVE;

	@Autowired
	private void encodeAndSetSecret(@Value("${SECRET}") String secret) {
		SECRET = Base64.getEncoder().encodeToString(secret.getBytes());
	}

	@Autowired
	private void setTimeToLive(@Value("${TIME_TO_LIVE}") String timeToLive) {
		TIME_TO_LIVE = Long.valueOf(timeToLive);
	}

	public static String getJwtToken(DbUser user) {
		Map<String, Object> claims = new HashMap<>();
		return generateToken(claims, String.valueOf(user.getId()));
	}

	public static Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		return Long.valueOf(claims.getSubject());
	}

	private static String generateToken(Map<String, Object> claims, String userId) {
		return Jwts.builder().setClaims(claims).setSubject(userId).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TIME_TO_LIVE))
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();
	}

	public static boolean isValidToken(String token, Long dbUserId) {
		Long userid = getUserIdFromToken(token);
		return userid.equals(dbUserId) && !isExpired(token);
	}

	public static boolean isExpired(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getExpiration()
				.before(new Date(System.currentTimeMillis()));
	}

}
