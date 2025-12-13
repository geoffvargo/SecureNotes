package com.geoffvargo.securenotes.controllers;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
	@Autowired
	private NoteService noteService;
	
	// CREATE
	@PostMapping
	public Note createNote(@RequestBody String content,
	                       @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		System.out.println("USER DETAILS: " + username);
		return noteService.createNoteForUser(username, content);
	}
	
	// READ
	@GetMapping
	public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		System.out.println("USER DETAILS: " + username);
		return noteService.getNotesForUser(username);
	}
	
	// UPDATE
	@PutMapping("/{noteId}")
	public Note updateNote(@PathVariable Long noteId,
	                       @RequestBody String content,
	                       @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		return noteService.updateNoteForUser(noteId, content, username);
	}
	
	// DELETE
	@DeleteMapping("/{noteId}")
	public void deleteNote(@PathVariable Long noteId,
	                       @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		noteService.deleteNoteForUser(noteId, username);
	}
}
