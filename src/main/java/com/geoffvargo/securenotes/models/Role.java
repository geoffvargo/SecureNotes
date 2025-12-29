package com.geoffvargo.securenotes.models;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId;
	
	@ToString.Exclude
	@Enumerated(EnumType.STRING)
	@Column(length = 20, name = "role_name")
	private AppRole roleName;
	
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JsonBackReference
	@ToString.Exclude
	private Set<User> users = new HashSet<>();
	
	public Role(AppRole roleName) {
		this.roleName = roleName;
	}
}
