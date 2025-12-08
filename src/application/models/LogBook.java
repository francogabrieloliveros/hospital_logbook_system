package application.models;

import java.time.LocalDateTime;

public class LogBook{
	private LocalDateTime timestamp;
	private String author;
	private String tag;
	private String message;
	
	public LogBook(String author, String tag, String message) {
		this.timestamp = LocalDateTime.now();
		this.author = author;
		this.tag = tag;
		this.message = message;
	}
	
	// Restore constructor
	public LogBook(LocalDateTime date, String author, String tag, String message) {
		this.timestamp = date;
		this.author = author;
		this.tag = tag;
		this.message = message;
	}
	
	// Getters
	public String toString() {
		return String.format("%s | author=%s | tag=%s | message=%s", 
				timestamp.toString(), author, tag, message);
	}
	public LocalDateTime getTimestamp() { return this.timestamp; }
	public String getAuthor() { return this.author; }
	public String getTag() { return this.tag; }
	public String getMessage() { return this.message; }
}