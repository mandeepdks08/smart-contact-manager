package main.restmodels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import main.annotations.NonNullable;
import main.datamodels.Address;
import main.enums.UserRole;

@Getter
@Setter
@Builder
public class UserRegisterRequest {
	@NonNullable
	private String userName;
	@NonNullable
	private String email;
	@NonNullable
	private String password;
	@NonNullable
	private String phone;
	@NonNullable
	private Address address;
	@NonNullable
	private UserRole role;
	private String about;
	private Boolean enabled;
	private String imageUrl;
}
