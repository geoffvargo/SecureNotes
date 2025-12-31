package com.geoffvargo.securenotes.repositories;

import com.geoffvargo.securenotes.models.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(AppRole roleName);
	
}
