package main.restmodels;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
	private String message;
	private Integer code;
	
	public BaseResponse(HttpStatus status) {
		this.code = status.value();
		this.message = (code >= 200 && code < 300) ? "SUCCESS" : "SOMETHING WENT WRONG";
	}
}
