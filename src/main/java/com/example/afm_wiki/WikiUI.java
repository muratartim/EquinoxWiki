/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.afm_wiki;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.example.afm_wiki.task.WikiTask;
import com.example.afm_wiki.ui.ProgressDialog;
import com.example.afm_wiki.ui.ResultsView;
import com.example.afm_wiki.ui.SearchView;
import com.example.afm_wiki.utility.AFMDatabaseConnection;
import com.example.afm_wiki.utility.Utility;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import snaq.db.ConnectionPool;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Push
public class WikiUI extends UI {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Progress dialog. */
	private ProgressDialog progressDialog_;

	/** File downloader. */
	private SimpleFileDownloader downloader_;

	/** Search view. */
	private SearchView searchView_;

	/** Results view. */
	private ResultsView resultsView_;

	/** The navigator of application. */
	private Navigator navigator_;

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		// set browser title
		getPage().setTitle("Digital Twin - WIKI");

		// create search view
		searchView_ = new SearchView(this);

		// create results view
		resultsView_ = new ResultsView(this);

		// create progress dialog
		progressDialog_ = new ProgressDialog(this);

		// create file downloader
		downloader_ = new SimpleFileDownloader();
		addExtension(downloader_);

		// create navigator and add views
		navigator_ = new Navigator(this, this);
		navigator_.addView("", searchView_);
		navigator_.addView("Results", resultsView_);
	}

	/**
	 * Returns the progress dialog.
	 *
	 * @return The progress dialog.
	 */
	public ProgressDialog getProgressDialog() {
		return progressDialog_;
	}

	/**
	 * Returns search view.
	 *
	 * @return The search view.
	 */
	public SearchView getSearchView() {
		return searchView_;
	}

	/**
	 * Returns results view.
	 *
	 * @return Results view.
	 */
	public ResultsView getResultsView() {
		return resultsView_;
	}

	/**
	 * Shows search view.
	 */
	public void showSearchView() {
		navigator_.navigateTo("");
	}

	/**
	 * Shows results view.
	 */
	public void showResultsView() {
		navigator_.navigateTo("Results");
	}

	/**
	 * Submits given task to thread pool.
	 *
	 * @param task
	 *            Task to submit.
	 */
	public void submitTask(WikiTask<?> task) {
		if (!progressDialog_.isAttached()) {
			addWindow(progressDialog_);
		}
		progressDialog_.submitTask(task);
	}

	/**
	 * Starts downloading given stream resource.
	 *
	 * @param downloadPath
	 *            Path to download file.
	 * @param fileName
	 *            Download file name.
	 */
	synchronized public void download(File downloadPath, String fileName) {

		// create stream resource
		StreamResource resource = new StreamResource(new StreamSource() {

			/** Serial ID. */
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {

				// create and return input stream
				try {
					return Files.newInputStream(downloadPath.toPath());
				}

				// exception occurred during process
				catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}, fileName);

		// start download
		downloader_.setFileDownloadResource(resource);
		downloader_.download();
	}

	@SuppressWarnings("serial")
	@WebListener
	@WebServlet(urlPatterns = "/*", name = "WikiUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = WikiUI.class, productionMode = true)
	public static class WikiUIServlet extends VaadinServlet implements ServletContextListener {

		@Override
		public void contextInitialized(ServletContextEvent arg0) {

			// get servlet context
			ServletContext servletContext = arg0.getServletContext();

			// create cashed thread pool and set it to servlet context
			servletContext.setAttribute("threadPool", Executors.newCachedThreadPool());
			System.out.println("Thread pool created.");

			// setup global database connection pool
			String hostname = AFMDatabaseConnection.HOSTNAME.getValue();
			String port = AFMDatabaseConnection.PORT.getValue();
			String path = AFMDatabaseConnection.PATH.getValue();
			String username = AFMDatabaseConnection.USERNAME.getValue();
			String password = AFMDatabaseConnection.PASSWORD.getValue();
			servletContext.setAttribute("databaseConnectionPool", Utility.setupDatabaseConnectionPool(hostname, port, path, username, password));
			System.out.println("Database connection pool created.");

			// servlet context initialized
			System.out.println("Servlet context initialized.");
		}

		@Override
		public void contextDestroyed(ServletContextEvent arg0) {

			// get servlet context
			ServletContext servletContext = arg0.getServletContext();

			// shut down thread pool
			ExecutorService threadpool = (ExecutorService) servletContext.getAttribute("threadPool");
			Utility.shutdownThreadExecutor(threadpool);
			System.out.println("Thread pool shut down.");

			// shutdown database connection pool
			ConnectionPool databaseConnectionPool = (ConnectionPool) servletContext.getAttribute("databaseConnectionPool");
			databaseConnectionPool.release();
			System.out.println("Database connection pool shutdown.");

			// clean downloads directory
			Utility.deleteTemporaryFiles(Utility.DOWNLOADS_DIR, Utility.DOWNLOADS_DIR);
			System.out.println("Downloads directory cleaned.");

			// servlet context destroyed
			System.out.println("Servlet context destroyed.");
		}
	}
}
