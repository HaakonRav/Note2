package com.cognitiveadventures.note;

public class Note {
	private long id;
	private String title;
	private boolean isChecked;
	
	public Note(Long id, String title) {
		
		this.id = id;
		this.title = title;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
