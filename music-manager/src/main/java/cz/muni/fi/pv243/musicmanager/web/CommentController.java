package cz.muni.fi.pv243.musicmanager.web;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.exceptions.ServiceException;
import cz.muni.fi.pv243.musicmanager.services.CommentService;

@Model
public class CommentController {
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private CommentService commentService;
	
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
}
