package main.datamodels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import main.annotations.NonNullable;

@Getter
@Setter
@Builder
public class Address {
	@NonNullable
	private String city;
	@NonNullable
	private String country;
	@NonNullable
	private String pinCode;
	@NonNullable
	private String address;
}
