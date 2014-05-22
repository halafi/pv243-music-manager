package cz.muni.fi.pv243.musicmanager.entities;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Class representing comment entity.
 * */
@Indexed(index="CommentIndex")
@ProvidedId
public class Comment implements Serializable{
	private static final long serialVersionUID = 3131025143284228441L;

	@Field
	private String id;
	
	@Field
	@NotNull
	private String authorUserName;
	
	@Field
	@NotNull
	private String songId;
	
	@Field(analyze=Analyze.NO)
	@DateBridge(resolution=Resolution.DAY)
	@NotNull
	private Date postTime;
	
	@Field
	@Size(min = 1, max = 250, message="{comment.text.size}")
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorUserName() {
		return authorUserName;
	}

	public void setAuthorUserName(String authorUserName) {
		this.authorUserName = authorUserName;
	}

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
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
		result = prime * result
				+ ((authorUserName == null) ? 0 : authorUserName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((postTime == null) ? 0 : postTime.hashCode());
		result = prime * result + ((songId == null) ? 0 : songId.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		if (authorUserName == null) {
			if (other.authorUserName != null)
				return false;
		} else if (!authorUserName.equals(other.authorUserName))
			return false;
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
		if (songId == null) {
			if (other.songId != null)
				return false;
		} else if (!songId.equals(other.songId))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", authorUserName=" + authorUserName
				+ ", songId=" + songId + ", postTime=" + postTime + ", text="
				+ text + "]";
	}
	
}
