package com.geoffvargo.securenotes.controllers;

import org.springframework.security.web.csrf.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;

@RestController
public class CsrfController {
	@GetMapping("/api/csrf-token")
	public CsrfToken csrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	}
}
