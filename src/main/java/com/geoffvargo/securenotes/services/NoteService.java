package com.geoffvargo.securenotes.services;

import com.geoffvargo.securenotes.models.*;

import java.util.*;

public interface NoteService {
	Note createNoteForUser(String username, String content);
	
	Note updateNoteForUser(Long noteId, String content, String username);
	
	void deleteNoteForUser(Long noteId, String username);
	
	List<Note> getNotesForUser(String username);
}
