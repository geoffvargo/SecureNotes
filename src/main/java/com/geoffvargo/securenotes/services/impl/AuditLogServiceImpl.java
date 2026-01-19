package com.geoffvargo.securenotes.services.impl;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;

@Service
class AuditLogServiceImpl implements AuditLogService {
	@Autowired
	AuditLogRepository auditLogRepository;
	
	@Override
	public void logNoteCreation(String username, Note note) {
		AuditLog log = new AuditLog();
		
		log.setAction("CREATE");
		log.setUsername(username);
		log.setNoteId(note.getId());
		log.setNoteContent(note.getContent());
		log.setTimestamp(LocalDateTime.now());
		
		auditLogRepository.save(log);
	}
	
	@Override
	public void logNoteUpdate(String username, Note note) {
		AuditLog log = new AuditLog();
		
		log.setAction("UPDATE");
		log.setUsername(username);
		log.setNoteId(note.getId());
		log.setNoteContent(note.getContent());
		log.setTimestamp(LocalDateTime.now());
		
		auditLogRepository.save(log);
	}
	
	@Override
	public void logNoteDeletion(String username, long noteId) {
		AuditLog log = new AuditLog();
		
		log.setAction("DELETE");
		log.setUsername(username);
		log.setNoteId(noteId);
		log.setTimestamp(LocalDateTime.now());
		
		auditLogRepository.save(log);
	}
}
