package com.geoffvargo.securenotes.repositories;

import com.geoffvargo.securenotes.models.*;

import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface NoteRepository extends JpaRepository<Note, Long> {
	List<Note> findByOwnerUsername(String username);
}
