package main.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import main.dbmodels.DbUser;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {

	private DbUser dbUser;

	public UserDetailsImpl(DbUser dbUser) {
		super();
		this.dbUser = dbUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority roles = new SimpleGrantedAuthority(this.dbUser.getRole());
		return Arrays.asList(roles);
	}

	@Override
	public String getPassword() {
		return this.dbUser.getPassword();
	}

	@Override
	public String getUsername() {
		return this.dbUser.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
