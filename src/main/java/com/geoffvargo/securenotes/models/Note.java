package com.geoffvargo.securenotes.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	private String content;
	
	private String ownerUsername;
}
