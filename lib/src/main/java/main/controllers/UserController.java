package main.controllers;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import main.utils.GsonUtils;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	protected BaseResponse register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
		DbUser dbUser = GsonUtils.convert(userRegisterRequest, DbUser.class);
		dbUser.setPassword(passwordEncoder.encode(dbUser.getPassword()));
		userRepo.save(dbUser);
		return new BaseResponse().success();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected BaseResponse login(@RequestBody @Valid UserLoginRequest loginRequest) throws CustomGeneralException {
		DbUser dbUser = userRepo.findByEmail(loginRequest.getEmail());
		if (Objects.isNull(dbUser) || !passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword())) {
			throw new CustomGeneralException("Either email or password is incorrect");
		}
		return new BaseResponse().success();
	}

}
