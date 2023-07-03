package main.security;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import main.dbmodels.DbUser;
import main.jparepositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		DbUser dbUser = userRepo.findByEmail(email);
		if (Objects.isNull(dbUser)) {
			throw new UsernameNotFoundException("Could not find user!");
		}
		return new UserDetailsImpl(dbUser);
	}

}
