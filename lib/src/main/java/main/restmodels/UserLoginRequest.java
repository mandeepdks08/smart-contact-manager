package main.restmodels;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserLoginRequest {
	@Email(message = "Please provide a valid email id.")
	private String email;
	@NotBlank(message = "Password is required.")
	private String password;
}
