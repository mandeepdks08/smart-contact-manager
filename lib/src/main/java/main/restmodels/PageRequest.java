package main.restmodels;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
	@NotNull(message = "Page number is required!")
	protected Integer page;
}
