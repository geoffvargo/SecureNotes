package com.geoffvargo.securenotes.repositories;

import com.geoffvargo.securenotes.models.*;

import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserName(String userName);
	Boolean existsByUserName(String username);
}
