package main.restmodels;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewContactSaveRequest {
	@NotBlank
	private String name;
	private String secondName;
	private String work;
	@Pattern(regexp = "^\\d{10}$", message = "Phone number can contain only numeric values and must be of 10 digits.")
	private String phone;
	@Email
	private String email;
	private String imageUrl;
	private String description;
}
