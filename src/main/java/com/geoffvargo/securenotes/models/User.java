package com.geoffvargo.securenotes.models;

import com.fasterxml.jackson.annotation.*;

import org.hibernate.annotations.*;

import java.time.*;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
       uniqueConstraints = {
	       @UniqueConstraint(columnNames = "username"),
	       @UniqueConstraint(columnNames = "email")
       })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	@NotBlank
	@Size(max = 20)
	@Column(name = "username")
	private String userName;
	
	@NotBlank
	@Size(max = 50)
	@Email
	@Column(name = "email")
	private String email;
	
	@Size(max = 120)
	@Column(name = "password")
	@JsonIgnore
	private String password;
	
	private boolean accountNonLocked = true;
	private boolean accountNonExpired = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	
	private LocalDate credentialsExpiryDate;
	private LocalDate accountExpiryDate;
	
	private String twoFactorSecret;
	private boolean isTwoFactorEnabled = false;
	private String signUpMethod;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "role_id", referencedColumnName = "role_id")
	@JsonBackReference
	@ToString.Exclude
	private Role role;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime updatedDate;
	
	public User(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	public User(String userName, String email) {
		this.userName = userName;
		this.email = email;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		return userId != null && userId.equals(((User) o).getUserId());
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
