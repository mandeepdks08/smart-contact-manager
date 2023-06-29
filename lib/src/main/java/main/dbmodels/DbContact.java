package main.dbmodels;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import main.datamodels.Address;

@Getter
@Setter
@Builder
@Entity
@Table(name = "contact")
public class DbContact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String name;
	private String phone;
	private String email;
	private Address address;
	private String about;
	private String imageUrl;
	private Boolean enabled;
}
