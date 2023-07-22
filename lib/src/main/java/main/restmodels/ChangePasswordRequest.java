package main.restmodels;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
	@NotBlank(message = "Old password is required.")
	private String oldPassword;
	@Size(min = 8, message = "Password should have at least 8 characters.")
	private String newPassword;
}
