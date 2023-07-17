package main.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import main.customExceptions.CustomGeneralException;
import main.dbmodels.DbUser;
import main.jparepositories.UserRepository;
import main.restmodels.BaseResponse;
import main.restmodels.UserLoginRequest;
import main.restmodels.UserRegisterRequest;
import main.security.JwtUtils;
import main.utils.GsonUtils;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authManager;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	protected BaseResponse register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
		DbUser dbUser = GsonUtils.convert(userRegisterRequest, DbUser.class);
		dbUser.setPassword(passwordEncoder.encode(dbUser.getPassword()));
		userRepo.save(dbUser);
		return BaseResponse.success();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest loginRequest, HttpServletResponse response)
			throws CustomGeneralException {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		if (!authentication.isAuthenticated()) {
			throw new CustomGeneralException("Bad credentials!");
		}
		String token = JwtUtils.getJwtToken(userRepo.findByEmail(loginRequest.getEmail()));
		response.addCookie(new Cookie("token", token));
		return new ResponseEntity<>(BaseResponse.success(), HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	protected ResponseEntity<?> update() {
		return null;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	protected ResponseEntity<?> delete() {
		return null;
	}

}
