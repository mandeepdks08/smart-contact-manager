package main.restmodels;

import lombok.Getter;
import lombok.Setter;
import main.annotations.NonNullable;

@Getter
@Setter
public class UserLoginRequest {
	@NonNullable
	private String username;
	@NonNullable
	private String password;
}
