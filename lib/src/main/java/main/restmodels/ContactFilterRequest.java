package main.restmodels;

import lombok.Getter;
import lombok.Setter;
import main.datamodels.BaseFilter;

@Getter
@Setter
public class ContactFilterRequest extends BaseFilter {
	private Long id;
	private String phone;
	private String email;
	private String name;
}
