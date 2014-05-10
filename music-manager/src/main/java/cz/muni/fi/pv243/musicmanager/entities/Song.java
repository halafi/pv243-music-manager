package cz.muni.fi.pv243.musicmanager.entities;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

/**
 * Represents a song. Song objects are stored in the cache.
 * @author filip
 */
@Indexed(index="SongIndex")
//@ProvidedId(name="id") should not be needed
public class Song {
	
	@Field
	@NotNull
	private String id;
	
	@Field
	@NotNull
	private String songName;
	
	// id of the uploader
	@Field
	@NotNull
	private String uploaderUserName;
	
	// id of the interpret
	@Field
	@NotNull
	private String interpretId; 
	
	@Field
	@NumericField
	private long timesPlayed;
	
	@Field
	private List<Comment> comments;
	
	//@Field
	@NotNull
	private String filePath; 
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSongName() {
		return songName;
	}
	
	public void setSongName(String songName) {
		this.songName = songName;
	}
	
	public String getUploaderUserName() {
		return uploaderUserName;
	}
	
	public void setUploaderUserName(String uploaderUserName) {
		this.uploaderUserName = uploaderUserName;
	}
	
	public String getInterpretId() {
		return interpretId;
	}
	
	public void setInterpretId(String interpretId) {
		this.interpretId = interpretId;
	}
	
	public long getTimesPlayed() {
		return timesPlayed;
	}
	
	public void setTimesPlayed(long timesPlayed) {
		this.timesPlayed = timesPlayed;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((interpretId == null) ? 0 : interpretId.hashCode());
		result = prime * result
				+ ((songName == null) ? 0 : songName.hashCode());
		result = prime * result + (int) (timesPlayed ^ (timesPlayed >>> 32));
		result = prime
				* result
				+ ((uploaderUserName == null) ? 0 : uploaderUserName.hashCode());
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
		Song other = (Song) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (interpretId == null) {
			if (other.interpretId != null)
				return false;
		} else if (!interpretId.equals(other.interpretId))
			return false;
		if (songName == null) {
			if (other.songName != null)
				return false;
		} else if (!songName.equals(other.songName))
			return false;
		if (timesPlayed != other.timesPlayed)
			return false;
		if (uploaderUserName == null) {
			if (other.uploaderUserName != null)
				return false;
		} else if (!uploaderUserName.equals(other.uploaderUserName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", songName=" + songName
				+ ", uploaderUserName=" + uploaderUserName + ", interpretId="
				+ interpretId + ", timesPlayed=" + timesPlayed + ", comments="
				+ comments + ", filePath=" + filePath + "]";
	}
	
}