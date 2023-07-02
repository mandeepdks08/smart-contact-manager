package main.globalExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import main.customExceptions.CustomGeneralException;
import main.datamodels.CustomError;
import main.restmodels.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { CustomGeneralException.class })
	public ResponseEntity<?> handleCustomGeneralException(CustomGeneralException ex) {
		BaseResponse baseResponse = new BaseResponse().failure();
		baseResponse.addError(CustomError.builder().message(ex.getMessage()).build());
		return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BaseResponse baseResponse = new BaseResponse().failure();
		ex.getFieldErrors().forEach(error -> {
			baseResponse.addError(
					CustomError.builder().field(error.getField()).message(error.getDefaultMessage()).build());
		});
		return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
	}
}
