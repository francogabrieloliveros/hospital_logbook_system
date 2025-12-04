package application.models;

import java.time.LocalDateTime;

public class LogBook{
	private Hospital hospital;
	private LocalDateTime timestamp;
	private String author;
	private String tag;
	private String message;
	
	public LogBook(Hospital hospital, String author, String tag, String message) {
		this.hospital = hospital;
		this.timestamp = LocalDateTime.now();
		this.author = author;
		this.tag = tag;
		this.message = message;
	}
	
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public String getMessage() {
		return this.message;
	}

	public void addLogToHospital(String message) {
		this.hospital.addLogBook(this);
	}
}