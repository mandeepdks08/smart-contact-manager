package main.enums;

public enum UserRole {
	ADMIN("AD"),
	CLIENT("CL");

	private String role;
	
	private UserRole(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return this.role;
	}
}
