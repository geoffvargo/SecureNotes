package com.geoffvargo.securenotes.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
	@GetMapping("/")
	public String index() {
		return "Hello World";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "Contact";
	}
}
