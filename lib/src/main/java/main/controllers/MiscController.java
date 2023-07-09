package main.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/misc")
public class MiscController {
	
	@RequestMapping(value = "/topsecret", method = RequestMethod.GET)
	protected String showTopSecret() {
		return "I pretend to be poor but I own half of my kidney.";
	}
}
