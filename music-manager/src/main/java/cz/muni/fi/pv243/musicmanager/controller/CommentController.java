package cz.muni.fi.pv243.musicmanager.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.CommentService;

@Model
public class CommentController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private CommentService commentService;
	
	@Inject
	private Logger logger;
	
	@Produces
	@Named
	private Comment newComment;
	
	@PostConstruct
	public void initComment(){
		newComment = new Comment();
	}
	
	public void addComment(){
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.jsf.Messages", facesContext.getViewRoot().getLocale());
		try {
			newComment.setAuthorUserName("Kouba");
			newComment.setPostTime(new Date());
			newComment.setSongId("4");
			commentService.createComment(newComment);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
									bundle.getString("comment.add.success.summary"),
									bundle.getString("comment.add.success.detail"));
			facesContext.addMessage(null, message);
			initComment();
		} catch (ServiceException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					bundle.getString("comment.add.fail.summary"),
					bundle.getString("comment.add.fail.detail"));
			facesContext.addMessage(null, message);
		}
	}
	
	public List<Comment> getCommentsBySong(){
		List<Comment> commentsBySong = new ArrayList<Comment>();
		try{
			logger.info("Getting comments by song.");
			commentsBySong = commentService.getCommentsBySongId("4");
			logger.info("Comments has been retrieved from cache!");
		} catch (ServiceException e) {
			logger.error("Error while getting comments from service, returning empty list.", e);
		}
		
		return commentsBySong;
	}
}
