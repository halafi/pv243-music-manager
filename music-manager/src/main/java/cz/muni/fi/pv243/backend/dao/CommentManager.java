package cz.muni.fi.pv243.backend.dao;

import java.util.List;

import cz.muni.fi.pv243.backend.entities.Comment;
import cz.muni.fi.pv243.backend.entities.Song;
import cz.muni.fi.pv243.exceptions.EntityExistsException;
import cz.muni.fi.pv243.exceptions.NonExistingEntityException;

/**
 * Interface for work with {@code Comment} entity.
 * */
public interface CommentManager {
	
	/**
	 * Puts new {@link Comment} to the cache store.
	 * @param comment new comment to be added
	 * @throws IllegalArgumentException when comment is null.
	 * @throws EntityExistsException if comment already exists in cache store.
	 * */
	public void createComment(Comment comment) throws EntityExistsException, IllegalArgumentException;
	
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
	 * @throws NonExistingEntityException TODO
	 * @throws IllegalArgumentException TODO
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
