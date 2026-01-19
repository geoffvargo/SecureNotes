package com.geoffvargo.securenotes.services.impl;

import com.geoffvargo.securenotes.models.*;

public interface AuditLogService {
	void logNoteCreation(String username, Note note);
	void logNoteUpdate(String username, Note note);
	void logNoteDeletion(String username, long noteId);
}
