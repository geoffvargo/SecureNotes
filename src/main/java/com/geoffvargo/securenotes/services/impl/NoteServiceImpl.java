package com.geoffvargo.securenotes.services.impl;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private NoteRepository noteRepository;
	
	@Override
	public Note createNoteForUser(String username, String content) {
		Note note = new Note();
		note.setOwnerUsername(username);
		note.setContent(content);
		return noteRepository.save(note);
	}
	
	@Override
	public Note updateNoteForUser(Long noteId, String content, String username) {
		Note note = noteRepository.findById(noteId)
			            .orElseThrow(
							() -> new RuntimeException("Note not found"));
		note.setContent(content);
		return noteRepository.save(note);
	}
	
	@Override
	public void deleteNoteForUser(Long noteId, String username) {
		noteRepository.deleteById(noteId);
	}
	
	@Override
	public List<Note> getNotesForUser(String username) {
		return noteRepository.findByOwnerUsername(username);
	}
}
