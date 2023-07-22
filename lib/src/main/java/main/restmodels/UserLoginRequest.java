package main.restmodels;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class UserLoginRequest {
	@Email(message = "Please provide a valid email id.")
	private String email;
	@NotBlank(message = "Password is required.")
	private String password;
	private String otp;
}
