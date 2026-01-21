package com.geoffvargo.securenotes.repositories;

import com.geoffvargo.securenotes.models.*;

import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findByNoteId(Long noteId);
}
