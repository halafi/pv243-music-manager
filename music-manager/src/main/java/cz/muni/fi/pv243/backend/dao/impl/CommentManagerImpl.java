package cz.muni.fi.pv243.backend.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.infinispan.commons.CacheException;
import org.infinispan.commons.api.BasicCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.pv243.backend.dao.CommentManager;
import cz.muni.fi.pv243.backend.entities.Comment;
import cz.muni.fi.pv243.exceptions.EntityExistsException;
import cz.muni.fi.pv243.exceptions.NonExistingEntityException;

/**
 * @author Radek Koubsky
 * */
@Stateless
public class CommentManagerImpl implements CommentManager {
	static final Logger logger = LoggerFactory.getLogger(CommentManagerImpl.class);
	
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
    private UserTransaction userTransaction;
	
	private BasicCache<String, Object> commentCache;
	
	@Override
	public void createComment(Comment comment) throws EntityExistsException,
			IllegalArgumentException {
		
		if(comment == null){
			throw new IllegalArgumentException("Comment is null.");
		}
		
		if(comment.getId() == null){
			throw new IllegalArgumentException("Comment id is null.");
		}
		commentCache = provider.getCacheContainer().getCache("commentcache");
		
		if(commentCache.containsKey(comment.getId())){
			throw new EntityExistsException("Comment already exists in cache.");
		}
		
		try {
			userTransaction.begin();
			commentCache.put(comment.getId(), comment);
			userTransaction.commit();
		} catch (Exception e) {
			logger.error("Error while putting comment to the comment cache.", e);
			throw new CacheException(e); 
		} 

	}

	@Override
	public Comment getCommentById(String id) {
		if(id == null){
			throw new IllegalArgumentException("Comment id is null.");
		}
		
		commentCache = provider.getCacheContainer().getCache("commentcache");
		if(commentCache.containsKey(id)){
			return (Comment)commentCache.get(id);
		} else {
			return null;
		}
	}

	@Override
	public void updateComment(Comment comment)
			throws NonExistingEntityException, IllegalArgumentException {

		if(comment == null){
			throw new IllegalArgumentException("Comment is null.");
		}
		
		if(comment.getId() == null){
			throw new IllegalArgumentException("Comment id is null.");
		}
		
		commentCache = provider.getCacheContainer().getCache("commentcache");
		
		if(!commentCache.containsKey(comment.getId())){
			throw new NonExistingEntityException("Comment does not exist in cache.");
		}
		
		try {
			userTransaction.begin();
			commentCache.put(comment.getId(), comment);
			userTransaction.commit();
		} catch (Exception e) {
			logger.error("Error while updating comment.", e);
			throw new CacheException(e); 
		}

	}

	@Override
	public void deleteComment(Comment comment) throws NonExistingEntityException, IllegalArgumentException {
		if(comment == null){
			throw new IllegalArgumentException("Comment is null.");
		}
		
		if(comment.getId() == null){
			throw new IllegalArgumentException("Comment id is null.");
		}
		
		commentCache = provider.getCacheContainer().getCache("commentcache");
		
		if(!commentCache.containsKey(comment.getId())){
			throw new NonExistingEntityException("Comment does not exist in cache.");
		}
		
		try {
			userTransaction.begin();
			commentCache.remove(comment.getId());
			userTransaction.commit();
		} catch (Exception e) {
			logger.error("Error while deleting comment.", e);
			throw new CacheException(e); 
		}

	}

	@Override
	public List<Comment> getCommentsBySongId(String songId) {
		// TODO Auto-generated method stub
		return null;
	}

}
