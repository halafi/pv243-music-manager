package cz.muni.fi.pv243.backend.dao.impl;

import java.util.List;
import java.util.UUID;

import cz.muni.fi.pv243.backend.dao.CommentManager;
import cz.muni.fi.pv243.backend.entities.Comment;
import cz.muni.fi.pv243.exceptions.EntityExistsException;
import cz.muni.fi.pv243.exceptions.NonExistingEntityException;

public class CommentManagerImpl implements CommentManager {

	@Override
	public void createComment(Comment comment) throws EntityExistsException,
			IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public Comment getCommentById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateComment(Comment comment)
			throws NonExistingEntityException, IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteComment(Comment comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Comment> getCommentsBySongId(UUID songId) {
		// TODO Auto-generated method stub
		return null;
	}

}
