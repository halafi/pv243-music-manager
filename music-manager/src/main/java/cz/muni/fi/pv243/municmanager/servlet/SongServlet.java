package cz.muni.fi.pv243.municmanager.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import cz.muni.fi.pv243.musicmanager.controller.SongController;

/**
 * Servlet implementation class SongServlet
 */
@WebServlet("/songs/*")
public class SongServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SongServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String filename = request.getPathInfo().substring(1);
			File file = new File(SongController.SONG_FOLDER, filename);
			response.setHeader("Content-Type",
					getServletContext().getMimeType(filename));
//			 response.setHeader("Content-Length",
	//		 String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\""
					+ filename + "\"");

			Files.copy(file.toPath(), response.getOutputStream());
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
