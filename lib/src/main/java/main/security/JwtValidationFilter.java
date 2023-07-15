package main.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import main.customExceptions.CustomGeneralException;
import main.dbmodels.DbUser;
import main.jparepositories.UserRepository;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepo;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return Arrays.stream(SecurityConstants.PUBLIC_ENDPOINTS)
				.anyMatch(publicUrl -> new AntPathMatcher().match(publicUrl, request.getServletPath()));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = fetchTokenFromHeaders(request);
			Long userId = JwtUtils.getUserIdFromToken(token);
			DbUser dbUser = userRepo.findById(userId).orElse(null);
			if (!JwtUtils.isValidToken(token, userId)) {
				throw new IOException("Unauthorized!");
			}
			UserDetails userDetails = new UserDetailsImpl(dbUser);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			throw new IOException("Unauthorized!");
		}
		filterChain.doFilter(request, response);
	}

	private String fetchTokenFromHeaders(HttpServletRequest request) throws CustomGeneralException {
		String token = request.getHeader("Authorization");
		if (StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
			throw new CustomGeneralException("Invalid token");
		}
		return token.substring(7);
	}

}
