package cz.muni.fi.pv243.musicmanager.dao;

import java.util.List;

import javax.ejb.Local;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;

/**
 * Interface for work with {@code Comment} entity.
 * */
@Local
public interface CommentManager {
	
	/**
	 * Puts new {@link Comment} to the cache store.
	 * @param comment new comment to be added
	 * @throws IllegalArgumentException when comment is null.
	 * @throws IllegalEntityException if comment id is already set.
	 * */
	public void createComment(Comment comment) throws IllegalEntityException, IllegalArgumentException;
	
	/**
	 * Retrieves {@link Comment} by id.
	 * @param id id of the comment to be retrieved
	 * @return Comment instance with specified id or null when Comment doesn't exists
	 * @throws IllegalArgumentException when comment id is null.
	 * */
	public Comment getCommentById(String id);
	
	/**
	 * Updates existing {@link Comment} in cache store.
	 * @param comment comment to be updated
	 * @throws IllegalArgumentException when comment is null.
	 * @throws NonExistingEntityException when Comment doesn't exist in cache store.
	 * */
	public void updateComment(Comment comment) throws NonExistingEntityException, IllegalArgumentException;
	
	/**
	 * Remove {@link Comment} from cache store.
	 * @param comment comment to be removed
	 * @throws IllegalArgumentException when comment is null.
	 * @throws NonExistingEntityException when Comment doesn't exist in cache store.
	 * */
	public void deleteComment(Comment comment) throws NonExistingEntityException, IllegalArgumentException;
	
	/**
	 * Retrieves list of all comments of {@link Song} specified by its id.
	 * @param id id of song
	 * @return List of comments of the song or null when song doesn't have any comment
	 * */
	public List<Comment> getCommentsBySongId(String songId);
}
