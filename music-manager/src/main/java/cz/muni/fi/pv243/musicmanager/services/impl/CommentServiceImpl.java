package cz.muni.fi.pv243.musicmanager.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.dao.CommentManager;
import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.CommentService;

@Stateless
public class CommentServiceImpl implements CommentService {

	@Inject
	private CommentManager commentManager;
	
	@Inject
    private Validator validator;
	
	@Inject
	private Logger logger;
	
	@Override
	public void createComment(Comment comment) throws ServiceException {
		try {
			validateComment(comment);
			commentManager.createComment(comment);
		} catch (Exception e) {
			logger.debug("Some exception was thrown by CommentManager or while validating comment.", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Comment getCommentById(String id) throws ServiceException {
		Comment comment = null;
		try {
			comment = commentManager.getCommentById(id);
		} catch (Exception e) {
			logger.debug("Some exception was thrown by CommentManager", e);
			throw new ServiceException(e);
		}
		
		return comment;
	}

	@Override
	public void updateComment(Comment comment) throws ServiceException {
		try {
			validateComment(comment);
			commentManager.updateComment(comment);
		} catch (Exception e) {
			logger.debug("Some exception was thrown by CommentManager or while validating comment.", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteComment(Comment comment) throws ServiceException {
		try {
			commentManager.deleteComment(comment);
		} catch (Exception e) {
			logger.debug("Some exception was thrown by CommentManager or while validating comment.", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Comment> getCommentsBySongId(String songId) throws ServiceException {
		List<Comment> result = new ArrayList<Comment>();
		try {
			result = commentManager.getCommentsBySongId(songId);
		} catch (Exception e) {
			logger.debug("Some exception was thrown by CommentManager.", e);
			throw new ServiceException(e);
		}
		
		return Collections.unmodifiableList(result);
	}
	
	/**
	 * Validates the given {@link Comment}
	 * @param comment Comment to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
	 * */
	private void validateComment(Comment comment) throws ConstraintViolationException {
		if(comment == null){
			throw new IllegalArgumentException("Comment is null.");
		}
		
		Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
		
		logger.info("Validation completed. violations found: " + violations.size());
	
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                new HashSet<ConstraintViolation<?>>(violations));
        }
	}
}
