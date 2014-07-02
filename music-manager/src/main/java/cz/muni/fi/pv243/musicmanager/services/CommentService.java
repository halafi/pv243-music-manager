package cz.muni.fi.pv243.musicmanager.services;

import java.util.List;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;

/**
 * @author Radek Koubsky
 * */
public interface CommentService {
	
	/**
	 * Creates new {@link Comment} in cache and sets its id.
	 * @param comment comment object to be created
	 * @throws ServiceException when any exception occurs on service layer
	 * */
	public void createComment(Comment comment) throws ServiceException;
	
	/**
	 * Retrieves {@link Comment} by id.
	 * @param id id of the comment to be retrieved
	 * @return Comment instance with specified id or null when Comment doesn't exists
	 * @throws ServiceException when any exception occurs on service layer
	 * */
	public Comment getCommentById(String id) throws ServiceException;
	
	/**
	 * Updates existing {@link Comment} in cache store.
	 * @param comment comment to be updated
	 * @throws ServiceException when any exception occurs on service layer
	 * */
	public void updateComment(Comment comment) throws ServiceException;
	
	/**
	 * Remove {@link Comment} from cache store.
	 * @param comment comment to be removed
	 * @throws ServiceException when any exception occurs on service layer
	 * */
	public void deleteComment(Comment comment) throws ServiceException;
	
	/**
	 * Retrieves list of all comments of {@link Song} specified by its id.
	 * @param id id of song
	 * @return List of comments of the song or null when song doesn't have any comment
	 * @throws ServiceException when any exception occurs on service layer
	 * */
	public List<Comment> getCommentsBySongId(String songId) throws ServiceException;
}
