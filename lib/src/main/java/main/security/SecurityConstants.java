package main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import main.utils.GsonUtils;

@Component
public class SecurityConstants {

	public static String[] PUBLIC_ENDPOINTS;
	public static String[] ADMIN_ONLY_ENDPOINTS;

	@Autowired
	private void init(@Value("${publicEndpoints}") String publicEndpoints,
			@Value("${adminOnlyEndpoints}") String adminOnlyEndpoints) {
		SecurityConstants.PUBLIC_ENDPOINTS = publicEndpoints.split(",");
		SecurityConstants.ADMIN_ONLY_ENDPOINTS = adminOnlyEndpoints.split(",");
		System.out.println(GsonUtils.getGson().toJson(PUBLIC_ENDPOINTS));
	}
}
