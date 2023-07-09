package main.globalExceptionHandlers;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import main.customExceptions.CustomGeneralException;
import main.datamodels.CustomError;
import main.restmodels.BaseResponse;
import main.utils.GsonUtils;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { CustomGeneralException.class })
	public ResponseEntity<?> handleCustomGeneralException(CustomGeneralException ex) {
		BaseResponse baseResponse = BaseResponse.failure();
		baseResponse.addError(CustomError.builder().message(ex.getMessage()).build());
		return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BaseResponse baseResponse = BaseResponse.failure();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			baseResponse
					.addError(CustomError.builder().field(error.getField()).message(error.getDefaultMessage()).build());
		});
		return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<?> handleAllExceptions(Exception ex) {
		BaseResponse baseResponse = BaseResponse.failure();
		String errormsg = Arrays.stream(ex.getStackTrace()).map(stackTrace -> stackTrace.toString()).collect(Collectors.joining("\n"));
		baseResponse.addError(CustomError.builder().message(errormsg).build());
		return new ResponseEntity<>(GsonUtils.getGson().toJson(ex.getStackTrace()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
