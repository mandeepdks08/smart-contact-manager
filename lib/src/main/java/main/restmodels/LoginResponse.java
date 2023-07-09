package main.restmodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
	private String jwt;
}
