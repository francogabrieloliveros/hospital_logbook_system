package application.models;

import java.time.LocalDateTime;

public class LogBook implements HospitalElement{
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
	
	public LocalDateTime getTimeStamp() {
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

	@Override
	public void addLogToHospital(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateId() {
		// TODO Auto-generated method stub
		return null;
	}
}
