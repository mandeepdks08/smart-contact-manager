package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import main.dbmodels.DbUser;
import main.jparepositories.UserRepository;
import main.security.UserDetailsImpl;

@Component
public class SystemContextHolder {

	private static UserRepository userRepo;

	@Autowired
	private void initializeUserRepo(UserRepository userRepo) {
		SystemContextHolder.userRepo = userRepo;
	}
	
	public static DbUser getLoggedInUser() {
		UserDetails user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepo.findByEmail(user.getUsername());
	}
}
