package main.restmodels;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.datamodels.CustomError;
import main.datamodels.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
	private List<CustomError> errors;
	private Status status;

	public void addError(CustomError error) {
		if (CollectionUtils.isEmpty(this.errors)) {
			errors = new ArrayList<>();
		}
		errors.add(error);
	}

	public BaseResponse success() {
		return BaseResponse.builder().status(Status.builder().success(true).httpStatus(HttpStatus.OK.value()).build())
				.build();
	}

	public BaseResponse failure() {
		return BaseResponse.builder()
				.status(Status.builder().success(false).httpStatus(HttpStatus.BAD_REQUEST.value()).build()).build();
	}
}
