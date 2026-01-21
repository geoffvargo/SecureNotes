package com.geoffvargo.securenotes.services;

import com.geoffvargo.securenotes.models.*;

import java.util.*;

public interface AuditLogService {
	void logNoteCreation(String username, Note note);
	void logNoteUpdate(String username, Note note);
	void logNoteDeletion(String username, long noteId);
	
	List<AuditLog> getAllAuditLogs();
	List<AuditLog> getAuditLogsForNoteId(Long id);
}
