package main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			String email = (String) authentication.getPrincipal();
			String password = (String) authentication.getCredentials();
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
			if (!passEncoder.matches(password, userDetails.getPassword())) {
				throw new RuntimeException("Bad credentials");
			}
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				SecurityContextHolder.getContext().setAuthentication(result);
			}
			return result;
		} catch (RuntimeException e) {
			throw new RuntimeException("Bad credentials!");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
