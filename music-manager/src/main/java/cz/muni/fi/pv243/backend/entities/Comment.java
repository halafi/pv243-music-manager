package cz.muni.fi.pv243.backend.entities;

import java.util.Date;
import java.util.UUID;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;
import org.hibernate.search.annotations.Resolution;

/**
 * Class representing comment entity.
 * */
@Indexed(index="CommentIndex")
@ProvidedId
public class Comment {
	
	@Field
	private String id;
	
	@Field
	private String authorUserName;
	
	@Field(analyze=Analyze.NO)
	@DateBridge(resolution=Resolution.DAY)
	private Date postTime;
	
	@Field
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return authorUserName;
	}

	public void setUsername(String username) {
		this.authorUserName = username;
	}

	public Date getPostTime() {
		if(postTime == null){
			return null;
		}
		return new Date(postTime.getTime());
	}

	public void setPostTime(Date postTime) {
		this.postTime = null;
		
		if(postTime != null){
			this.postTime = new Date(postTime.getTime());
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
				+ ((authorUserName == null) ? 0 : authorUserName.hashCode());
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
		if (authorUserName == null) {
			if (other.authorUserName != null)
				return false;
		} else if (!authorUserName.equals(other.authorUserName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", authorUserName=" + authorUserName + ", postTime="
				+ postTime + ", text=" + text + "]";
	}
}
