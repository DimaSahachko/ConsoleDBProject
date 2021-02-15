package com.sahachko.consoledbproject.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {
	private Long id;
	private String content;
	private Date created;
	private Date updated;
	
	public Post(String content) {
		this.content = content;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY 'at' HH:mm:ss");
		if((created == null) && (updated == null)) {
			return id + "." + content;	
		} else if(updated == null) {
			return id + "." + content + ". (Created: " + dateFormat.format(created) + ")";
		} else {
		return  id + "." + content + ". (Created: " + dateFormat.format(created) + ". Updated: " + dateFormat.format(updated) + ")";
		}
	}
}
