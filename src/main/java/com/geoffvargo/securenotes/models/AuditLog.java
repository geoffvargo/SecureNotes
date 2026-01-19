package com.geoffvargo.securenotes.models;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class AuditLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String action;
	private String username;
	private Long noteId;
	private String noteContent;
	private LocalDateTime timestamp;
}
