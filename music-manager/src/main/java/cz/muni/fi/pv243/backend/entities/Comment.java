package cz.muni.fi.pv243.backend.entities;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;

/**
 * Class representing comment entity.
 * */
@Indexed(index="CommentIndex")
@ProvidedId
public class Comment {
	
	@Field
	private UUID id;
	
	@Field
	private String author;
	
	@Field
	private Timestamp postTime;
	
	@Field
	private String text;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return author;
	}

	public void setUsername(String username) {
		this.author = username;
	}

	public Timestamp getPostTime() {
		if(postTime == null){
			return null;
		}
		return new Timestamp(postTime.getTime());
	}

	public void setPostTime(Timestamp postTime) {
		this.postTime = null;
		
		if(postTime != null){
			this.postTime = new Timestamp(postTime.getTime());
		}
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((postTime == null) ? 0 : postTime.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result
				+ ((author == null) ? 0 : author.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (postTime == null) {
			if (other.postTime != null)
				return false;
		} else if (!postTime.equals(other.postTime))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", username=" + author + ", postTime="
				+ postTime + ", text=" + text + "]";
	}
}
