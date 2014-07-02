package cz.muni.fi.pv243.musicmanager.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.entities.Comment;
import cz.muni.fi.pv243.musicmanager.entities.Song;
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
	private ResourceBundle bundle;

	@Inject
	private Logger logger;

	@Inject
	private SongController songController;

	@Produces
	@Named
	private Comment newComment;
	@Produces
	@Named
	private Song editSong;
	
	@PostConstruct
	public void initComment() {
		newComment = new Comment();
		editSong = new Song();
	}

	public void addComment(String songId) {
		ResourceBundle bundle = ResourceBundle.getBundle("i18n.jsf.Messages",
				facesContext.getViewRoot().getLocale());
		try {
			newComment.setAuthorUserName("Kouba");
			newComment.setPostTime(new Date());
			newComment.setSongId(songId);

			commentService.createComment(newComment);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					bundle.getString("comment.add.success.summary"),
					bundle.getString("comment.add.success.detail"));
			facesContext.addMessage(null, message);
			initComment();
			songController.details(songId);
		} catch (ServiceException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					bundle.getString("comment.add.fail.summary"),
					bundle.getString("comment.add.fail.detail"));
			facesContext.addMessage(null, message);
			songController.details(songId);
		}
	}

	public List<Comment> getCommentsBySong() {
		List<Comment> commentsBySong = new ArrayList<Comment>();
		try {
			//TODO: to this another way if there's time
			String songId = 
					(String) facesContext.getExternalContext().getSessionMap().get("songId");
			logger.info("Getting comments by song.");
			commentsBySong = commentService.getCommentsBySongId(songId);
			logger.info("Comments has been retrieved from cache!");
		} catch (ServiceException e) {
			logger.error(
					"Error while getting comments from service, returning empty list.",
					e);
		}

		return commentsBySong;
	}

	public String displayComments(String id) {
			//TODO: to this other way if there is time
			facesContext.getExternalContext().getSessionMap().put("songId", id);
			editSong.setId(id);
		return "comments";
	}
	public void removeComment(String id) {
		try {
			Comment comment = commentService.getCommentById(id);
			commentService.deleteComment(comment);

			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, bundle
							.getString("comment.remove.success"), null));
			
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("comment.remove.fail.service"), null));
		}
	}

	public String edit(String id) {

		try {
			newComment = commentService.getCommentById(id);
			songController.edit(newComment.getId());
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("comment.get.fail.service"), null));
		}
		return "edit_comment";
	}

	public void initEditPage(ComponentSystemEvent event) {
		Map<String, String> params = facesContext.getExternalContext()
				.getRequestParameterMap();
		String commentId = params.get("comment_id");

		try {
			newComment = commentService.getCommentById(commentId);
		} catch (ServiceException e) {
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("comment.get.fail.service"), null));
		}
	}
}
