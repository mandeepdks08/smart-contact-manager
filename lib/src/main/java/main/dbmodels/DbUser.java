package main.dbmodels;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "phone"}))
public class DbUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	private String phone;
	private String role;
	@Column(length = 500)
	private String about;
	private Boolean enabled;
	private String imageUrl;
	
	@Builder.Default
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbUser")
	private List<DbContact> contacts = new ArrayList<>();
	
}
