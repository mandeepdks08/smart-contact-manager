package main.controllers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

import main.SystemContextHolder;
import main.customExceptions.CustomGeneralException;
import main.dbmodels.DbUser;
import main.jparepositories.UserRepository;
import main.restmodels.BaseResponse;
import main.restmodels.ChangePasswordRequest;
import main.restmodels.UserLoginRequest;
import main.restmodels.UserRegisterRequest;
import main.security.JwtUtils;
import main.utils.GenericUtils;
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

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final long OTP_EXPIRATION_TIME_SECONDS = 300;

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
		if (StringUtils.isEmpty(loginRequest.getOtp())) {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			if (!authentication.isAuthenticated()) {
				throw new CustomGeneralException("Bad credentials!");
			}
			DbUser dbUser = userRepo.findByEmail(loginRequest.getEmail());
			String storedOtp = (String) redisTemplate.opsForValue().get(String.valueOf(dbUser.getId()));
			if (StringUtils.isNotBlank(storedOtp)) {
				long expiration = redisTemplate.getExpire(String.valueOf(dbUser.getId()), TimeUnit.SECONDS);
				if (OTP_EXPIRATION_TIME_SECONDS - expiration < 60) {
					throw new CustomGeneralException("Please try again after " + (expiration % 240) + " seconds!");
				}
			}
			String otp = GenericUtils.generateOtp();
			redisTemplate.opsForValue().set(String.valueOf(dbUser.getId()), otp, OTP_EXPIRATION_TIME_SECONDS,
					TimeUnit.SECONDS);
			// send OTP to user's email
			System.out.println(String.valueOf(dbUser.getEmail()) + ": " + otp);
		} else {
			String otp = loginRequest.getOtp();
			String email = loginRequest.getEmail();
			DbUser dbUser = userRepo.findByEmail(email);
			if (Objects.isNull(dbUser)) {
				throw new CustomGeneralException("Bad credentials!");
			}
			String storedOtp = (String) redisTemplate.opsForValue().get(String.valueOf(dbUser.getId()));
			if (!otp.equals(storedOtp)) {
				throw new CustomGeneralException("Invalid OTP!");
			}
			redisTemplate.delete(String.valueOf(dbUser.getId()));
			String token = JwtUtils.getJwtToken(dbUser);
			response.addCookie(new Cookie("token", token));
		}
		return new ResponseEntity<>(BaseResponse.success(), HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	protected ResponseEntity<?> update(@RequestBody DbUser updateRequest) throws CustomGeneralException {
		updateRequest.setId(null);
		updateRequest.setPassword(null);
		DbUser dbUser = userRepo.findById(SystemContextHolder.getLoggedInUser().getId()).get();
		DbUser updatedUser = GenericUtils.update(dbUser, updateRequest);
		userRepo.save(updatedUser);
		return ResponseEntity.ok(BaseResponse.success());
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	protected ResponseEntity<?> delete() {
		return null;
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.PATCH)
	protected ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest req)
			throws CustomGeneralException {
		String oldPassword = req.getOldPassword();
		DbUser dbUser = SystemContextHolder.getLoggedInUser();
		if (!passwordEncoder.matches(oldPassword, dbUser.getPassword())) {
			throw new CustomGeneralException("Wrong old password");
		}
		dbUser.setPassword(passwordEncoder.encode(req.getNewPassword()));
		userRepo.save(dbUser);
		return ResponseEntity.ok(BaseResponse.success());
	}

}
