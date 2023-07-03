package main.restmodels;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
	@Pattern(regexp = "^[\\p{L} .'-]+$", message = "Please provide a valid name.")
	private String name;
	@Email(message = "Please provide a valid email.")
	private String email;
	@Size(min = 8, message = "Password should have at least 8 characters.")
	private String password;
	@Pattern(regexp = "^\\d{10}$", message = "Phone number can contain only numeric values and must be of 10 digits.")
	private String phone;
	private String role;
	private String about;
	private Boolean enabled;
	private String imageUrl;
}
