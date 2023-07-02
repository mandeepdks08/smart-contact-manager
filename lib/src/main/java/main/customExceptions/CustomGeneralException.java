package main.customExceptions;

import lombok.Getter;

@Getter
public class CustomGeneralException extends Exception {
	private String message;
	
	public CustomGeneralException(String message) {
		this.message = message;
	}
}
