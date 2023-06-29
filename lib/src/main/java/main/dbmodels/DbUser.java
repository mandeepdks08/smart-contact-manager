package main.dbmodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import main.datamodels.Address;
import main.enums.UserRole;

@Getter
@Setter
@Builder
@Entity
@Table(name = "\"USER\"", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "phone"}))
public class DbUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userName;
	private String email;
	private String password;
	private String phone;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private Address address;
	@Type(type = "varchar")
	@Column(columnDefinition = "varchar")
	private UserRole role;
	private String about;
	private Boolean enabled;
	private String imageUrl;
	
}
