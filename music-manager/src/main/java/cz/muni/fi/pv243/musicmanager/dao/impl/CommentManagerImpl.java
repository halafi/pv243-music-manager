package cz.muni.fi.pv243.musicmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.commons.CacheException;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.muni.fi.pv243.musicmanager.dao.CommentManager;
import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.exceptions.IllegalEntityException;
import cz.muni.fi.pv243.musicmanager.exceptions.NonExistingEntityException;
import cz.muni.fi.pv243.musicmanager.utils.UUIDStringGenerator;

/**
 * @author Radek Koubsky
 * */
@Stateless
@javax.ejb.TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class CommentManagerImpl implements CommentManager {
	@Inject
	private Logger logger;
	
	@Inject
	private CacheContainerProvider provider;
	
	@Inject
    private UserTransaction userTransaction;
	
	private BasicCache<String, Object> commentCache;
	
	@Override
	public void createComment(Comment comment) throws IllegalEntityException,
			IllegalArgumentException, CacheException {
		
		if(comment == null){
			throw new IllegalArgumentException("Comment is null.");
		}
		
		if(comment.getId() != null){
			throw new IllegalEntityException("Comment id is not null, Comment entity cannot be put into cache.");
		}
		commentCache = provider.getCacheContainer().getCache("commentcache");
		comment.setId(UUIDStringGenerator.generateCommentId());
		
		try {
			userTransaction.begin();
			commentCache.put(comment.getId(), comment);
			userTransaction.commit();
			logger.info("Comment with id: " + comment.getId() + " was inserted to cache store.");
		} catch (Exception e) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
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
			logger.info("Comment with id: " + comment.getId() + " was updated in cache store.");
		} catch (Exception e) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
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
			logger.info("Comment was deleted from cache store.");
		} catch (Exception e) {
			if(userTransaction != null){
				try {
					userTransaction.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			logger.error("Error while deleting comment.", e);
			throw new CacheException(e); 
		}

	}

	@Override
	public List<Comment> getCommentsBySongId(String songId) {
		if(songId == null){
			throw new IllegalArgumentException("Song id is null.");
		}
		ArrayList<Comment> comments = new ArrayList<Comment>();
		commentCache = provider.getCacheContainer().getCache("commentcache");
		SearchManager sm = Search.getSearchManager((Cache<String, Object>) commentCache);
		QueryBuilder queryBuilder = sm.buildQueryBuilderForClass(Comment.class).get();
		
		Query q = queryBuilder.keyword().onField("songId").matching(songId).createQuery();
		logger.debug("Lucene query: " + q);
		
		CacheQuery cq = sm.getQuery(q, Comment.class);
		
		for (Object o : cq.list()) {
	          if (o instanceof Comment) {
	             comments.add(((Comment) o));
	          }
	       }
	       return comments;
	}

}
