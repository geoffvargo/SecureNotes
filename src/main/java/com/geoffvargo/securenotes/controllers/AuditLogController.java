package com.geoffvargo.securenotes.controllers;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {
	@Autowired
	AuditLogService auditLogService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<AuditLog> getAuditLog() {
		return auditLogService.getAllAuditLogs();
	}
	
	@GetMapping("/note/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<AuditLog> getNoteAuditLogs(@PathVariable Long id) {
		return auditLogService.getAuditLogsForNoteId(id);
	}
}
