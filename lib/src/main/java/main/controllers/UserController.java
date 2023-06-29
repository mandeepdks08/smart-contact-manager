package main.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import main.dbmodels.DbUser;
import main.jparepositories.UserRepository;
import main.restmodels.BaseResponse;
import main.restmodels.UserLoginRequest;
import main.restmodels.UserRegisterRequest;
import main.utils.GsonUtils;
import main.validators.Validator;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	protected BaseResponse register(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
		Validator.validateNonNullFields(userRegisterRequest);
		String jsonString = GsonUtils.getGson().toJson(userRegisterRequest);
		DbUser dbUser = GsonUtils.getGson().fromJson(jsonString, DbUser.class);
		userRepo.save(dbUser);
		return new BaseResponse(HttpStatus.OK);
	}
	
	protected BaseResponse login(@RequestBody UserLoginRequest loginRequest) throws Exception {
		Validator.validateNonNullFields(loginRequest);
		DbUser dbUser = userRepo.findByUserName(loginRequest.getUsername());
		if(Objects.nonNull(dbUser) && dbUser.getPassword().equals(loginRequest.getPassword())) {
			return new BaseResponse(HttpStatus.OK);
		} else {
			return new BaseResponse(HttpStatus.UNAUTHORIZED);
		}
	}

}
